package pillihuaman.com.security.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pillihuaman.com.basebd.common.MyJsonWebToken;
import pillihuaman.com.basebd.control.dao.ControlDAO;
import pillihuaman.com.basebd.token.Token;
import pillihuaman.com.basebd.token.TokenType;
import pillihuaman.com.basebd.token.dao.TokenRepository;
import pillihuaman.com.basebd.user.User;
import pillihuaman.com.basebd.user.dao.UserRepository;
import pillihuaman.com.lib.exception.CustomRestExceptionHandlerGeneric;
import pillihuaman.com.lib.exception.UnprocessableEntityException;
import pillihuaman.com.lib.request.ReqUser;
import pillihuaman.com.lib.response.RespBase;
import pillihuaman.com.lib.response.RespUser;
import pillihuaman.com.lib.response.ResponseUser;
import pillihuaman.com.security.config.JwtService;
import pillihuaman.com.security.user.mapper.ControlMapper;
import pillihuaman.com.security.user.mapper.UserMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    @Autowired
    private UserDetailsService userInfoUserDetailsServiceimplements;

    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ControlDAO controlDAO;
    @Autowired
    private CustomRestExceptionHandlerGeneric exceptionHandler;

    public AuthenticationResponse register(ReqUser request) {
        String passwor = (passwordEncoder.encode(request.getPassword()));
        var user = User.builder()
                .userName(request.getUserName())
                // .userNameP(request.getUserName())
                //.user(request.getUser())
                .alias(request.getAlias())
                .email(request.getEmail())
                .mobilPhone(request.getMobilPhone())
                // .idSystem(new ArrayList<>())
                .passwordP(passwor)
                .password(passwor)
                // .role(Role.ADMIN)
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
            RespBase<AuthenticationResponse> response = new RespBase<>();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            List<User> lsr = repository.findUserName(request.getEmail());
            RespUser re = UserMapper.INSTANCE.toRespUser(lsr.get(0));
            var jwtToken = jwtService.generateToken(lsr.get(0));
            var refreshToken = jwtService.generateRefreshToken(lsr.get(0));

            var control = ControlMapper.INSTANCE.controlsToRespControls(controlDAO.findByUser(lsr.get(0)));
            revokeAllUserTokens(lsr.get(0));
            saveUserToken(lsr.get(0), jwtToken);
            return RespBase.builder().payload(AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(re)
                    .controls(control)
                    .build()).trace(RespBase.Trace.builder().traceId("1").build()).status(RespBase.Status.builder().success(true).error(null).build()).build();
        } catch (Exception ex) {
            throw new UnprocessableEntityException("Authentication failed " + ex.getMessage());
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
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        // tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmail(userEmail)
                    .orElseThrow();
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

    public MyJsonWebToken
    getUserFromToken(String token) {
        String tokensun=tokenSUb(token);
        Claims claims = jwtService.extractAllClaims(tokensun);
        String username = jwtService.extractUsername(tokensun);
        Optional<User> userDetails = repository.findByEmail(username);
        MyJsonWebToken toke = new MyJsonWebToken();
        toke.setUser(ResponseUser.builder()._id(userDetails.get().getId()).mail(userDetails.get().getEmail()).
                username(userDetails.get().getUsername()).mobil_Phone(userDetails.get().getMobilPhone()).build());
        return toke;
    }

    public String tokenSUb(String request) {
        final String jwt;
        final String userEmail;
        if (request == null || request.startsWith("Bearer ")) {
            assert request != null;
            jwt = request.substring(7);
            return jwt;
        }
        return "";
    }
}
