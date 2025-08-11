package pillihuaman.com.pe.security.provider;


import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.repository.GenericNotificationServiceImpl.NotificationDetails;
import pillihuaman.com.pe.security.service.NotificationProvider;


@Slf4j
@Component("EMAILNotificationProvider") // <--- Nombre del Bean para el Strategy
@RequiredArgsConstructor
public class EmailNotificationProvider implements NotificationProvider {

    private final JavaMailSender mailSender;
    @Value("${application.email.sender.from}")
    private String fromEmail;
    @Value("${application.email.sender.name}")
    private String senderName;

    @Override
    public void send(NotificationDetails details) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(fromEmail, senderName);
            helper.setTo(details.to());
            helper.setSubject(details.subject());
            helper.setText(details.body(), true);
            log.info("Enviando correo a: {}", details.to());
            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente a: {}", details.to());
        } catch (Exception e) {
            log.error("Error al enviar email a [{}]: {}", details.to(), e.getMessage());
            throw new RuntimeException("Fallo en el envío del correo", e);
        }
    }
}