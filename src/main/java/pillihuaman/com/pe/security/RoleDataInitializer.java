package pillihuaman.com.pe.security;

import jakarta.annotation.PostConstruct;
import org.bson.types.ObjectId;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.repository.RoleRepository;

import java.util.Date;
import java.util.List;
//@Component
//@RequiredArgsConstructor
public class RoleDataInitializer {

   // private final RoleRepository roleRepository;
    private  RoleRepository roleRepository;
    @PostConstruct
    public void insertDefaultRoles() {
        if (roleRepository.countAll() == 0) {
            AuditEntity audit = AuditEntity.builder()
                    .mail("alamodaperu1.online@gmail.com")
                    .mailUpdate("system")
                    .dateRegister(new Date())
                    .dateUpdate(new Date())
                    .build();

            List<Roles> defaultRoles = List.of(
                    Roles.builder().id(new ObjectId()).name("ADMIN")
                            .description("Administrador con acceso total")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("USER")
                            .description("Usuario estándar del sistema")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("DEV")
                            .description("Desarrollador de software")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("QA")
                            .description("Especialista en control de calidad")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("DEVOPS")
                            .description("Encargado de DevOps y despliegues")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("ANALYST")
                            .description("Analista de datos o negocio")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("SUPPORT")
                            .description("Personal de soporte técnico")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),

                    Roles.builder().id(new ObjectId()).name("MANAGER")
                            .description("Gerente de equipo o proyecto")
                            .active(true).permissionIds(List.of()).assignedUsers(List.of()).audit(audit).build(),
                    Roles.builder()
                            .id(new ObjectId())
                            .name("ANONYMOUS")
                            .description("Usuario anónimo de Internet con acceso público limitado")
                            .active(true)
                            .permissionIds(List.of())
                            .assignedUsers(List.of())
                            .audit(audit)
                            .build()
            );

            for (Roles role : defaultRoles) {
                roleRepository.save(role);
            }

            System.out.println("✅ Roles profesionales insertados correctamente al iniciar.");
        } else {
            System.out.println("✔️ Ya existen roles en la base de datos.");
        }
    }
}
