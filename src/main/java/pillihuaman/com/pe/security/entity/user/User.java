package pillihuaman.com.pe.security.entity.user;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.entity.token.Token;
import pillihuaman.com.pe.lib.common.AuditEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Document(collection = "user")
public class User implements UserDetails {

    @Id
    private ObjectId id;
    private AuditEntity auditEntity;
    private String alias;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true, sparse = true)
    private String mobilPhone;
    private String userName;
    private String apiPassword;
    private String passwordP;
    private String password;
    private String salPassword;
    private boolean enabled;
    private String name;
    private String typeDocument;
    @Indexed(unique = true, sparse = true)
    private String numTypeDocument;
    private List<Roles> roles;
    private List<System> system;
    private UserSetting customSettings;
    private List<Token> tokens;
    private String verificationCode;
    private String tenantId;
    private LocalDateTime verificationCodeExpires;

    @Builder.Default
    private boolean emailVerified = false;
    @Builder.Default
    private boolean phoneVerified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email; // Puedes usar el 'email' o el 'userName' como nombre de usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Asumiendo que la cuenta no tiene fecha de expiración
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Asumiendo que la cuenta no está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Asumiendo que las credenciales no han expirado
    }

    @Override
    public boolean isEnabled() {
        return enabled;  // Retorna el valor de la propiedad 'enabled'
    }
    @Override
    public String getPassword() {
        return password;  // Retorna la contraseña del usuario
    }

    public ObjectId getId() {
        return id;
    }

    public AuditEntity getAuditEntity() {
        return auditEntity;
    }

    public String getAlias() {
        return alias;
    }

    public String getMobilPhone() {
        return mobilPhone;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getApiPassword() {
        return apiPassword;
    }

    public String getPasswordP() {
        return passwordP;
    }

    public String getSalPassword() {
        return salPassword;
    }

    public String getName() {
        return name;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public String getNumTypeDocument() {
        return numTypeDocument;
    }


    public UserSetting getCustomSettings() {
        return customSettings;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public User( ) {

    }
}
