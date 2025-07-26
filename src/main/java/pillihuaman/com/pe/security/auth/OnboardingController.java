package pillihuaman.com.pe.security.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pillihuaman.com.pe.lib.common.RespBase;
import pillihuaman.com.pe.security.dto.AuthenticationResponse;
import pillihuaman.com.pe.security.dto.OnboardingRequestDTO;
import pillihuaman.com.pe.security.dto.VerifyCodeRequestDTO;
import pillihuaman.com.pe.security.service.OnboardingService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
public class OnboardingController {

    private final OnboardingService onboardingService;

    /**
     * Inicia el proceso de registro/login.
     * El frontend envía el email y teléfono.
     */
    @PostMapping("/request-code")
    public ResponseEntity<RespBase<Object>> requestCode(@Valid @RequestBody OnboardingRequestDTO request) {
        onboardingService.processOnboardingRequest(request);

        RespBase<Object> response = new RespBase<>();
        response.setPayload(Map.of("message", "Se ha enviado un código a tu correo y teléfono."));
        response.getStatus().setSuccess(true);
        return ResponseEntity.ok(response);
    }

    /**
     * Confirma la identidad con el código y obtiene los tokens de acceso.
     */
    @PostMapping("/verify")
    public ResponseEntity<RespBase<AuthenticationResponse>> verify(@Valid @RequestBody VerifyCodeRequestDTO request) {
        AuthenticationResponse authResponse = onboardingService.verifyCodeAndLogin(request);

        RespBase<AuthenticationResponse> response = new RespBase<>();
        response.setPayload(authResponse);
        response.getStatus().setSuccess(true);
        return ResponseEntity.ok(response);
    }
}