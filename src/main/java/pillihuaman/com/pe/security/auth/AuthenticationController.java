package pillihuaman.com.pe.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pillihuaman.com.pe.security.dto.AuthenticationRequest;
import pillihuaman.com.pe.security.dto.AuthenticationResponse;
import pillihuaman.com.pe.security.dto.ReqUser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService service;
    private final List<String> lisError = new ArrayList<>();

    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody ReqUser request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ) {

        Object result = service.authenticate(request);
        return ResponseEntity.ok(result);

    }

    @GetMapping("/getUserByToken")
    public ResponseEntity<?> getUser(
            @RequestParam String request
    ) {

        Object result = service.getUserFromToken(request);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        
        service.refreshToken(request, response);
    }


}
