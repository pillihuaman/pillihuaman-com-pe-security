package pillihuaman.com.pe.security;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.repository.RoleRepository;
import pillihuaman.com.pe.security.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
/*
@Component
@RequiredArgsConstructor*/
public class UserDataInitializer {

    private  UserRepository userRepository;
    private  RoleRepository roleRepository;
    private  PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAnonymousUserIfNotExists() {
        String anonymousEmail = "pillihuamanhz@alamodaperu.online";
        Optional<User> existing = userRepository.findByEmail(anonymousEmail);

        if (existing.isEmpty()) {

            List<Roles> anonymousRoles = roleRepository.findByName("ANONYMOUS");

            if (anonymousRoles == null || anonymousRoles.isEmpty()) {
                System.err.println("❌ No se encontró el rol 'ANONYMOUS'. Verifica la inicialización de roles.");
                return;
            }

            Roles anonymousRole = anonymousRoles.get(0);  // Tomar el primer rol

            AuditEntity audit = AuditEntity.builder()
                    .mail(anonymousEmail)
                    .mailUpdate("system")
                    .dateRegister(new Date())
                    .dateUpdate(new Date())
                    .build();

            User anonymousUser = User.builder()
                    .userName("anonymous")
                    .alias("Invitado")
                    .email(anonymousEmail)
                    .mobilPhone("")
                    .password("anonymous")
                    .passwordP(passwordEncoder.encode("anonymous"))
                    .roles(List.of(anonymousRole))
                    .auditEntity(audit)
                    .enabled(true)
                    .build();

            userRepository.saveUser(anonymousUser, null);
            System.out.println("✅ Usuario anónimo registrado correctamente.");
        } else {
            System.out.println("✔️ Usuario anónimo ya existe.");
        }
    }
}
