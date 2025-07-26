package pillihuaman.com.pe.security.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OnboardingRequestDTO {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String mobilPhone; // Incluye validaciones de formato si lo deseas
}