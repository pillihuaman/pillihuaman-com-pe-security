package pillihuaman.com.pe.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String accessToken;
  private String refreshToken;
  private RespUser user;
  private List<RespControl> controls;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public RespUser getUser() {
    return user;
  }

  public void setUser(RespUser user) {
    this.user = user;
  }

  public List<RespControl> getControls() {
    return controls;
  }

  public void setControls(List<RespControl> controls) {
    this.controls = controls;
  }
}
