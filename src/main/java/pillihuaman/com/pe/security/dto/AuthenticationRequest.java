package pillihuaman.com.pe.security.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;
  private String password;
    private ClientAuditInfo clientInfo;
}
