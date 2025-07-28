package pillihuaman.com.pe.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;
import pillihuaman.com.pe.lib.exception.UnprocessableEntityException;
import pillihuaman.com.pe.security.JwtService;
import pillihuaman.com.pe.security.dto.*;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.entity.token.Token;
import pillihuaman.com.pe.security.entity.token.TokenType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.repository.ControlRepository;
import pillihuaman.com.pe.security.repository.TokenRepository;
import pillihuaman.com.pe.security.repository.UserRepository;
import pillihuaman.com.pe.security.service.AuditDataService;
import pillihuaman.com.pe.security.user.mapper.ControlMapper;
import pillihuaman.com.pe.security.user.mapper.UserMapper;
import pillihuaman.com.pe.security.util.MyJsonWebToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository repository; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private TokenRepository tokenRepository; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private JwtService jwtService; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private AuthenticationManager authenticationManager; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private ControlRepository controlDAO; // Inyectado con @RequiredArgsConstructor
    @Autowired
    private CustomRestExceptionHandlerGeneric exceptionHandler; // Inyectado con @Autowired
    @Autowired
    private UserDetailsService userInfoUserDetailsServiceimplements; // Inyectado con @Autowired
    private final AuditDataService auditDataService;


    public AuthenticationResponse register(ReqUser request, HttpServletRequest httpRequest) { // Aceptar httpRequest
        String password = passwordEncoder.encode(request.getPassword());

        var user = User.builder()
                .userName(request.getUserName())
                .alias(request.getAlias())
                .email(request.getEmail())
                .mobilPhone(request.getMobilPhone())
                .passwordP(password)
                .password(password)
                .roles(convertRolesRequestToRoles(request.getRoles()))
                .build();

        var savedUser = repository.saveUser(user, null);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Pasa la información de auditoría. El clientInfo es null en este flujo.
        saveUserToken(savedUser, jwtToken, httpRequest, "REGISTER", null);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(UserMapper.INSTANCE.toRespUser(savedUser))
                .build();
    }


    public Object authenticate(AuthenticationRequest request, HttpServletRequest httpRequest) { // Aceptar httpRequest
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Simplificamos la búsqueda del usuario
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UnprocessableEntityException("Usuario no encontrado."));

            RespUser respUser = UserMapper.INSTANCE.toRespUser(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            var controls = ControlMapper.INSTANCE.controlsToRespControls(controlDAO.findByUser(user));

            revokeAllUserTokens(user);
            // Pasa el ClientAuditInfo del DTO
            saveUserToken(user, jwtToken, httpRequest, "PASSWORD_AUTH", request.getClientInfo());

            return RespBase.builder()
                    .payload(AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .user(respUser)
                            .controls(controls)
                            .build())
                    .trace(RespBase.Trace.builder().traceId("1").build())
                    .status(RespBase.Status.builder().success(true).build())
                    .build();
        } catch (Exception ex) {
            throw new UnprocessableEntityException("Authentication failed: " + ex.getMessage());
        }
    }

    // ▼▼▼ MÉTODO generateGuestToken ACTUALIZADO ▼▼▼
    public Object generateGuestToken(HttpServletRequest httpRequest) { // Aceptar httpRequest
        try {
            String guestEmail = "pillihuamanhz@alamodaperu.online";
            User mainGuestUser = repository.findByEmail(guestEmail)
                    .orElseThrow(() -> new UnprocessableEntityException("Guest user not found in database."));

            var jwtToken = jwtService.generateToken(mainGuestUser);
            var refreshToken = jwtService.generateRefreshToken(mainGuestUser);
            var controls = ControlMapper.INSTANCE.controlsToRespControls(controlDAO.findByUser(mainGuestUser));

            revokeAllUserTokens(mainGuestUser);
            // El clientInfo es null para el invitado
            saveUserToken(mainGuestUser, jwtToken, httpRequest, "GUEST_AUTH", null);

            RespUser respUser = UserMapper.INSTANCE.toRespUser(mainGuestUser);

            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(respUser)
                    .controls(controls)
                    .build();

            return RespBase.builder()
                    .payload(authResponse)
                    .trace(RespBase.Trace.builder().traceId("guest-token").build())
                    .status(RespBase.Status.builder().success(true).build())
                    .build();

        } catch (Exception ex) {
            throw new UnprocessableEntityException("Guest token generation failed: " + ex.getMessage());
        }
    }

    private List<Roles> convertRolesRequestToRoles(List<RolesRequest> rolesRequestList) {
        if (rolesRequestList == null) return List.of();
        return rolesRequestList.stream().map(req ->
                Roles.builder()
                        .id(new ObjectId(req.getId()))  // Asegúrate de que el DTO tenga el ID
                        .name(req.getName())
                        .description(req.getDescription())
                        .active(true) // O usa req.getActive() si aplica
                        .build()
        ).toList();
    }

    private void saveUserToken(User user, String jwtToken, HttpServletRequest request, String authMethod, ClientAuditInfo clientInfo) {
        // 1. Construir la entidad de auditoría completa
        AuditEntity audit = auditDataService.buildAuditEntity(request, clientInfo, user.getEmail(), authMethod);

        // 2. Crear el objeto Token
        var token = Token.builder()
                .token(jwtToken) // Guarda el JWT como ID del documento
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .audit(audit) // Asigna la entidad de auditoría enriquecida
                .build();

        // 3. Guardar en la base de datos
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = repository.findByEmail(userEmail)
                    .orElseThrow(() -> new UnprocessableEntityException("Usuario no encontrado para refresh token."));

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                // También pasamos el request aquí para auditar la renovación
                saveUserToken(user, accessToken, request, "REFRESH_TOKEN", null);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
    public MyJsonWebToken getUserFromToken(String token) {
        String tokenSub = tokenSUb(token);
        Claims claims = jwtService.extractAllClaims(tokenSub);
        String username = jwtService.extractUsername(tokenSub);

        Optional<User> userDetails = repository.findByEmail(username);
        return MyJsonWebToken.builder()
                .user(ResponseUser.builder()
                        .id(userDetails.get().getId())
                        .mail(userDetails.get().getEmail())
                        .username(userDetails.get().getUsername())
                        .mobilPhone(userDetails.get().getMobilPhone())
                        .build())
                .build();
    }

    public String tokenSUb(String request) {
        if (request != null && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        return "";
    }
    public AuthenticationResponse generateAndSaveTokens(User user, HttpServletRequest request) { // Aceptar httpRequest
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        // El clientInfo es null porque este flujo viene del onboarding (backend)
        saveUserToken(user, jwtToken, request, "ONBOARDING_VERIFY", null);

        RespUser respUser = UserMapper.INSTANCE.toRespUser(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(respUser)
                .build();
    }

}
