package pillihuaman.com.pe.security.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class VerifyCodeRequestDTO {
    @NotEmpty
    private String identifier; // Puede ser el email O el teléfono

    @NotEmpty
    private String code; // El código de 4 dígitos
}