package pillihuaman.com.security.auth;

import pillihuaman.com.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private Role role;
  private Object idUser;
  private String alias;
  private String apiPassword;
  private Object idSystem;
  private String mail;
  private String mobilPhone;
  private String salPassword;
  private String user;
  private String username;
  private String typeDocument;
  private String numTypeDocument;
}
