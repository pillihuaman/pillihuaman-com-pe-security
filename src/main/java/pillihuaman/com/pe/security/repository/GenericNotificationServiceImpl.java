package pillihuaman.com.pe.security.repository;

import com.azure.cosmos.implementation.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pillihuaman.com.pe.security.EmailTemplatesConfig;
import pillihuaman.com.pe.security.dto.NotificationRequestDTO;
import pillihuaman.com.pe.security.entity.notify.NotificationTemplate;
import pillihuaman.com.pe.security.service.GenericNotificationService;
import pillihuaman.com.pe.security.service.NotificationProvider;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericNotificationServiceImpl implements GenericNotificationService {

        private final Map<String, NotificationProvider> providerMap;
        private final NotificationTemplateRepository templateRepository;
        private final TemplateEngine templateEngine;
        private final EmailTemplatesConfig emailTemplatesConfig; // <-- Nuevo

        public record NotificationDetails(String to, String subject, String body) {}

        @Override
        @Async
        public void dispatchNotification(NotificationRequestDTO request) {
            log.info("Procesando notificación para plantilla [{}] vía [{}]",
                    request.getTemplateId(), request.getChannel());
            try {
                String content;
                String subject;

                // 1. Buscar en BD
                NotificationTemplate template = templateRepository
                        .findByTemplateIdAndChannelAndLanguage(request.getTemplateId(),
                                request.getChannel(), request.getLanguage())
                        .orElse(null);

                if (template != null) {
                    content = template.getContent();
                    subject = template.getSubject();
                } else {
                    // 2. Buscar en archivo YML
                    log.info("Plantilla [{}] no encontrada en BD, buscando en archivo...", request.getTemplateId());
                    content = emailTemplatesConfig.getTemplates().get(request.getTemplateId());
                    if (content == null) {
                        throw new NotFoundException("Plantilla no encontrada: " + request.getTemplateId());
                    }
                    // Si no tienes subject en el YML, puedes generar uno básico
                    subject = "¡Bienvenido a nuestra plataforma!";
                }

                // 3. Procesar HTML con Thymeleaf
                String body = processTemplate(content, request.getPayload());
                String finalSubject = processTemplate(subject, request.getPayload());

                // 4. Seleccionar proveedor
                NotificationProvider provider = providerMap.get(request.getChannel().name() + "NotificationProvider");
                if (provider == null) {
                    throw new IllegalArgumentException("Proveedor no encontrado para el canal: " + request.getChannel());
                }

                // 5. Enviar
                provider.send(new NotificationDetails(request.getRecipient(), finalSubject, body));

            } catch (Exception e) {
                log.error("Fallo al despachar notificación para [{}]: {}", request.getRecipient(), e.getMessage(), e);
            }
        }

        private String processTemplate(String content, Map<String, Object> payload) {
            if (content == null || payload == null) return content;

            String adaptedContent = content.replaceAll("\\{\\{\\s*(\\w+)\\s*\\}\\}", "[[\\${$1}]]");

            Context context = new Context();
            context.setVariables(payload);

            return templateEngine.process(adaptedContent, context);
        }
    }

    /*POST /api/v1/notifications/send
Content-Type: application/json

{
  "payload": {
    "recipient": "cliente@ejemplo.com",
    "templateId": "welcome-contact",
    "channel": "EMAIL",
    "language": "es",
    "payload": {
      "platformName": "MiPlataforma",
      "name": "Carlos",
      "ctaUrl": "https://mi-plataforma.com/descubrir",
      "year": "2025"
    }
  }
}
*/
