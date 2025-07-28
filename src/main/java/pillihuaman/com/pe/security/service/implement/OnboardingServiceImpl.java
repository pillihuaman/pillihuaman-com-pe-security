package pillihuaman.com.pe.security.service.implement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document; // <-- Importación necesaria
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pillihuaman.com.pe.lib.exception.UnprocessableEntityException;
import pillihuaman.com.pe.security.auth.AuthenticationService;
import pillihuaman.com.pe.security.dto.AuthenticationResponse;
import pillihuaman.com.pe.security.dto.OnboardingRequestDTO;
import pillihuaman.com.pe.security.dto.VerifyCodeRequestDTO;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.entity.user.User;
import pillihuaman.com.pe.security.repository.RoleRepository;
import pillihuaman.com.pe.security.repository.UserRepository;
import pillihuaman.com.pe.security.service.NotificationService;
import pillihuaman.com.pe.security.service.OnboardingService;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_USER_ROLE = "ANONYMOUS";

    @Override
    @Transactional
    public void processOnboardingRequest(OnboardingRequestDTO request) {
        // Usamos nuestro nuevo método del repositorio. Es más limpio y eficiente.
        User user = userRepository.findByEmailOrMobilPhone(request.getEmail(), request.getMobilPhone())
                .orElse(null);

        String code = new DecimalFormat("0000").format(new SecureRandom().nextInt(10000));

        if (user == null) {
            // --- FLUJO: CREAR NUEVO USUARIO ---
            log.info("Usuario no encontrado con email [{}] o teléfono [{}]. Creando nuevo usuario.", request.getEmail(), request.getMobilPhone());

            List<Roles> defaultRole = roleRepository.findByName(DEFAULT_USER_ROLE);

            User newUser = User.builder()
                    .email(request.getEmail())
                    .mobilPhone(request.getMobilPhone())
                    .userName(request.getEmail())
                    .roles(defaultRole)
                    .enabled(false)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .verificationCode(code) // Asignamos el código
                    .verificationCodeExpires(LocalDateTime.now().plusMinutes(5)) // y su expiración
                    .build();

            // Usamos el método específico para insertar, que ya tienes
            userRepository.insertOne(newUser);
            log.info("Nuevo usuario creado y código de verificación [{}] generado.", code);

            // Enviar notificación al nuevo usuario
            notificationService.sendVerificationCode(newUser.getEmail(), newUser.getMobilPhone(), code);

        } else {
            // --- FLUJO: ACTUALIZAR USUARIO EXISTENTE ---
            log.info("Usuario existente encontrado con ID [{}]. Actualizando código de verificación.", user.getId());

            user.setVerificationCode(code);
            user.setVerificationCodeExpires(LocalDateTime.now().plusMinutes(5));

            // Creamos un filtro para encontrar el documento por su _id
            Document filter = new Document("_id", user.getId());

            // Usamos el método específico para actualizar, que ya tienes
            userRepository.updateOne(filter, user);
            log.info("Código de verificación [{}] actualizado para el usuario [{}].", code, user.getId());

            // Enviar notificación al usuario existente
            notificationService.sendVerificationCode(user.getEmail(), user.getMobilPhone(), code);
        }
    }
    @Override
    @Transactional
    public AuthenticationResponse verifyCodeAndLogin(VerifyCodeRequestDTO request) {
        log.info("Iniciando verificación para el identificador: {}", request.getIdentifier());

        User user = userRepository.findByEmailOrMobilPhone(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new UnprocessableEntityException("Usuario no encontrado. Verifique el email o teléfono."));

        // ... (Validaciones de código y expiración sin cambios)
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.getCode())) {
            throw new UnprocessableEntityException("El código de verificación es inválido.");
        }
        if (user.getVerificationCodeExpires().isBefore(LocalDateTime.now())) {
            throw new UnprocessableEntityException("El código de verificación ha expirado. Por favor, solicita uno nuevo.");
        }

        log.info("Código verificado exitosamente para el usuario [{}]. Habilitando cuenta.", user.getId());
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpires(null);
        if (request.getIdentifier().equals(user.getEmail())) user.setEmailVerified(true);
        if (request.getIdentifier().equals(user.getMobilPhone())) user.setPhoneVerified(true);

        Document filter = new Document("_id", user.getId());
        userRepository.updateOne(filter, user);
        log.info("Usuario [{}] actualizado a estado verificado.", user.getId());

        // CORRECCIÓN: Obtenemos la HttpServletRequest actual del contexto de Spring
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("Generando tokens para el usuario verificado [{}].", user.getId());
        // Pasamos el request al método, solucionando el error de compilación
        return authenticationService.generateAndSaveTokens(user, httpRequest);
    }
}