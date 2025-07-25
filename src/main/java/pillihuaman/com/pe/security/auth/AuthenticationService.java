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

    public AuthenticationResponse register(ReqUser request) {
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
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(UserMapper.INSTANCE.toRespUser(savedUser))
                .build();
    }

    public Object authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = repository.findByEmail(request.getEmail()).orElseThrow();
            List<User> lsr = repository.findUserName(request.getEmail());
            RespUser respUser = UserMapper.INSTANCE.toRespUser(lsr.get(0));
            var jwtToken = jwtService.generateToken(lsr.get(0));
            var refreshToken = jwtService.generateRefreshToken(lsr.get(0));
            var controls = ControlMapper.INSTANCE.controlsToRespControls(controlDAO.findByUser(lsr.get(0)));

            revokeAllUserTokens(lsr.get(0));
            saveUserToken(lsr.get(0), jwtToken);

            return RespBase.builder()
                    .payload(AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .user(respUser)
                            .controls(controls)
                            .build())
                    .trace(RespBase.Trace.builder().traceId("1").build())
                    .status(RespBase.Status.builder().success(true).error(null).build())
                    .build();
        } catch (Exception ex) {
            throw new UnprocessableEntityException("Authentication failed " + ex.getMessage());
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

    public Object generateGuestToken() {
        try {
            // Buscar al usuario invitado
            String guestEmail = "pillihuamanhz@alamodaperu.online"; // puedes usar un campo fijo
            User mainGuestUser = repository.findByEmail(guestEmail)
                    .orElseThrow(() -> new UnprocessableEntityException("Guest user not found in database."));

            // Obtener lista por nombre si es parte de tu lógica (opcional)
            // List<User> usersByName = repository.findUserName(guestEmail);
            // User mainGuestUser = usersByName.isEmpty() ? guestUser : usersByName.get(0);

            // Generar tokens
            String jwtToken = jwtService.generateToken(mainGuestUser);
            String refreshToken = jwtService.generateRefreshToken(mainGuestUser);

            // Obtener controles si aplica
            var controls = ControlMapper.INSTANCE.controlsToRespControls(controlDAO.findByUser(mainGuestUser));

            // Revocar tokens anteriores y guardar el nuevo
            revokeAllUserTokens(mainGuestUser);
            saveUserToken(mainGuestUser, jwtToken);

            // Mapear user
            RespUser respUser = UserMapper.INSTANCE.toRespUser(mainGuestUser);

            // Construir respuesta
            AuthenticationResponse authResponse = AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(respUser)
                    .controls(controls)
                    .build();

            return RespBase.builder()
                    .payload(authResponse)
                    .trace(RespBase.Trace.builder().traceId("guest-token").build())
                    .status(RespBase.Status.builder().success(true).error(null).build())
                    .build();

        } catch (Exception ex) {
            throw new UnprocessableEntityException("Guest token generation failed: " + ex.getMessage());
        }
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
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
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = repository.findByEmail(userEmail).orElseThrow();
            List<User> lsr = repository.findUserName(userEmail);

            if (jwtService.isTokenValid(refreshToken, lsr.get(0))) {
                var accessToken = jwtService.generateToken(lsr.get(0));
                revokeAllUserTokens(lsr.get(0));
                saveUserToken(lsr.get(0), accessToken);

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

    public AuthenticationResponse generateAndSaveTokens(User user) {
        // 1. Generar tokens
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 2. Revocar tokens anteriores
        revokeAllUserTokens(user);

        // 3. Guardar nuevo token
        saveUserToken(user, jwtToken);

        // 4. Mapear al DTO de usuario de respuesta
        RespUser respUser = UserMapper.INSTANCE.toRespUser(user);

        // 5. Construir respuesta
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(respUser)
                .build();
    }


}
