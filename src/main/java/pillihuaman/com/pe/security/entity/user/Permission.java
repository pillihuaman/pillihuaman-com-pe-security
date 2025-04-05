package pillihuaman.com.pe.security.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

    private final String permission;

    // 🔹 Constructor privado correcto para inicializar el enum
    private Permission(String permission) {
        this.permission = permission;
    }
}
