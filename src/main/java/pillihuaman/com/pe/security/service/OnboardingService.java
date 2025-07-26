package pillihuaman.com.pe.security.service;


import pillihuaman.com.pe.security.dto.AuthenticationResponse;
import pillihuaman.com.pe.security.dto.OnboardingRequestDTO;
import pillihuaman.com.pe.security.dto.VerifyCodeRequestDTO;

public interface OnboardingService {

    /**
     * Procesa una solicitud de onboarding. Si el usuario no existe, lo crea.
     * En ambos casos, genera y envía un código de 4 dígitos.
     */
    void processOnboardingRequest(OnboardingRequestDTO request);

    /**
     * Valida el código proporcionado. Si es correcto, activa al usuario
     * y devuelve los tokens de autenticación.
     */
    AuthenticationResponse verifyCodeAndLogin(VerifyCodeRequestDTO request);
}