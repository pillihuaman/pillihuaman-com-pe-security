package pillihuaman.com.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.lib.response.RespControl;
import pillihuaman.com.lib.response.RespUser;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;
  @JsonProperty("refresh_token")
  private String refreshToken;
  @JsonProperty("user")
  private RespUser user;
  @JsonProperty("controls")
  private List<RespControl> controls;


}
