package pillihuaman.com.pe.security.repository;

import com.azure.cosmos.implementation.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import pillihuaman.com.pe.security.dto.NotificationRequestDTO;
import pillihuaman.com.pe.security.entity.notify.NotificationTemplate;
import pillihuaman.com.pe.security.service.GenericNotificationService;
import pillihuaman.com.pe.security.service.NotificationProvider;
import org.thymeleaf.context.Context;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericNotificationServiceImpl implements GenericNotificationService {

    private final Map<String, NotificationProvider> providerMap;
    private final NotificationTemplateRepository templateRepository;
    private final TemplateEngine templateEngine;

    // DTO interno para pasar datos procesados a los proveedores
    public record NotificationDetails(String to, String subject, String body) {}

    @Override
    @Async
    public void dispatchNotification(NotificationRequestDTO request) {
        log.info("Procesando notificación para plantilla [{}] vía [{}]", request.getTemplateId(), request.getChannel());
        try {
            // 1. Obtener la plantilla
            NotificationTemplate template = templateRepository
                    .findByTemplateIdAndChannelAndLanguage(request.getTemplateId(), request.getChannel(), request.getLanguage())
                    .orElseThrow(() -> new NotFoundException("Plantilla no encontrada: " + request.getTemplateId()));

            // 2. Procesar contenido y asunto con Thymeleaf
            String body = processTemplate(template.getContent(), request.getPayload());
            String subject = processTemplate(template.getSubject(), request.getPayload());

            // 3. Seleccionar el proveedor correcto usando el nombre del bean (Strategy)
            NotificationProvider provider = providerMap.get(request.getChannel().name() + "NotificationProvider");
            if (provider == null) {
                throw new IllegalArgumentException("Proveedor no encontrado para el canal: " + request.getChannel());
            }

            // 4. Enviar
            provider.send(new NotificationDetails(request.getRecipient(), subject, body));

        } catch (Exception e) {
            log.error("Fallo al despachar notificación para [{}]: {}", request.getRecipient(), e.getMessage(), e);
        }
    }
    private String processTemplate(String content, Map<String, Object> payload) {
        if (content == null || payload == null) return content;

        // Reemplazar sintaxis tipo {{var}} por [[${var}]]
        String adaptedContent = content.replaceAll("\\{\\{\\s*(\\w+)\\s*\\}\\}", "[[\\${$1}]]");

        Context context = new Context();
        context.setVariables(payload);

        return templateEngine.process(adaptedContent, context);
    }

}