package pillihuaman.com.pe.security.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional // Buena práctica para asegurar que todas las operaciones de DB se completen o fallen juntas
    public void processOnboardingRequest(OnboardingRequestDTO request) {
        // Usamos nuestro nuevo método del repositorio. Es más limpio y eficiente.
        User user = userRepository.findByEmailOrMobilPhone(request.getEmail(), request.getMobilPhone())
                .orElse(null);

        if (user == null) {
            log.info("Usuario no encontrado con email [{}] o teléfono [{}]. Creando nuevo usuario.", request.getEmail(), request.getMobilPhone());
            // -- FLUJO: Usuario NUEVO --
            List<Roles> defaultRole = roleRepository.findByName(DEFAULT_USER_ROLE);


            user = User.builder()
                    .email(request.getEmail())
                    .mobilPhone(request.getMobilPhone())
                    .userName(request.getEmail()) // Se puede mejorar para usar un alias único
                    .roles(defaultRole)
                    .enabled(false) // El usuario nace deshabilitado hasta la verificación
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
        } else {
            log.info("Usuario existente encontrado con ID [{}]. Actualizando código de verificación.", user.getId());
        }

        // -- LÓGICA COMÚN --
        String code = new DecimalFormat("0000").format(new SecureRandom().nextInt(10000));
        user.setVerificationCode(code);
        user.setVerificationCodeExpires(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user); // Guarda el usuario nuevo o actualiza el existente
        log.info("Código de verificación [{}] generado para el usuario [{}].", code, user.getId());

        // Enviar notificaciones por ambos canales (el servicio se encarga de la lógica)
        notificationService.sendVerificationCode(user.getEmail(), user.getMobilPhone(), code);
    }

    @Override
    @Transactional
    public AuthenticationResponse verifyCodeAndLogin(VerifyCodeRequestDTO request) {
        log.info("Iniciando verificación para el identificador: {}", request.getIdentifier());

        // 1. Buscar al usuario por email O teléfono usando el identificador
        User user = userRepository.findByEmailOrMobilPhone(request.getIdentifier(), request.getIdentifier())
                .orElseThrow(() -> new UnprocessableEntityException("Usuario no encontrado. Verifique el email o teléfono."));

        // 2. Validar el código de verificación
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(request.getCode())) {
            log.warn("Intento de verificación fallido para el usuario [{}]: código inválido.", user.getId());
            throw new UnprocessableEntityException("El código de verificación es inválido.");
        }

        // 3. Validar que el código no haya expirado
        if (user.getVerificationCodeExpires().isBefore(LocalDateTime.now())) {
            log.warn("Intento de verificación fallido para el usuario [{}]: código expirado.", user.getId());
            throw new UnprocessableEntityException("El código de verificación ha expirado. Por favor, solicita uno nuevo.");
        }

        // 4. ¡Éxito! Habilitar al usuario y limpiar los campos del código
        log.info("Código verificado exitosamente para el usuario [{}]. Habilitando cuenta.", user.getId());
        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpires(null);

        // Opcional: Marcar email o teléfono como verificado
        if (request.getIdentifier().equals(user.getEmail())) {
            user.setEmailVerified(true);
        }
        if (request.getIdentifier().equals(user.getMobilPhone())) {
            user.setPhoneVerified(true);
        }

        // 5. Guardar los cambios en el usuario
        userRepository.save(user);

        // 6. Generar y devolver los tokens de autenticación
        log.info("Generando tokens para el usuario verificado [{}].", user.getId());
        // (Asumiendo que creaste el método 'generateAndSaveTokens' en el paso anterior)
        return authenticationService.generateAndSaveTokens(user);
    }

}