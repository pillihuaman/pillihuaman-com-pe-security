package pillihuaman.com.pe.security.entity.token;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.entity.user.User;


import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Component
@Document(collection = "token")
public class Token implements Serializable  {

  private static final long serialVersionUID = 1L;
  @BsonProperty(value = "_id")
  public String token;
  public TokenType tokenType = TokenType.BEARER;
  public boolean revoked;
  public boolean expired;
  public User user;

  public Token() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
