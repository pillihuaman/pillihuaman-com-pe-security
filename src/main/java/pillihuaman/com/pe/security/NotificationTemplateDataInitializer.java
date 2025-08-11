package pillihuaman.com.pe.security;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.entity.notify.ChannelType;
import pillihuaman.com.pe.security.entity.notify.NotificationTemplate;
import pillihuaman.com.pe.security.repository.NotificationTemplateRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationTemplateDataInitializer {

    private final NotificationTemplateRepository templateRepository;


    public void insertDefaultTemplates() {

            List<NotificationTemplate> templates = List.of(
                    NotificationTemplate.builder()
                            .id(new ObjectId().toString())
                            .templateId("NEW_CONTACT_FORM")
                            .description("Plantilla para nuevo formulario de contacto")
                            .subject("¡Bienvenido a nuestra plataforma, {{nombre}}!")
                            .content("<html><body><h1>Hola {{nombre}}</h1><p>Gracias por unirte a nosotros.</p></body></html>")
                            .channel(ChannelType.EMAIL)
                            .language("es")
                            .build(),

                    NotificationTemplate.builder()
                            .id(new ObjectId().toString())
                            .templateId("VERIFICATION_CODE")
                            .description("Plantilla para código de verificación")
                            .subject("Tu código de verificación es {{codigo}}")
                            .content("<html><body><p>Hola {{nombre}}, tu código de verificación es: <strong>{{codigo}}</strong></p></body></html>")
                            .channel(ChannelType.EMAIL)
                            .language("es")
                            .build()
            );

            templates.forEach(templateRepository::save);
            System.out.println("✅ Plantillas de notificación insertadas correctamente.");

    }
}
