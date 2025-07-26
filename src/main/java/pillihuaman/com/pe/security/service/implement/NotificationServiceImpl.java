package pillihuaman.com.pe.security.service.implement;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pillihuaman.com.pe.security.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    // --- Dependencias de Correo ---
    private final JavaMailSender mailSender;
    @Value("${application.email.sender.from}")
    private String fromEmail;
    @Value("${application.email.sender.name}")
    private String senderName;

    // --- Dependencias de Twilio ---
   /* @Value("${twilio.account-sid}")
    private String twilioAccountSid;
    @Value("${twilio.auth-token}")
    private String twilioAuthToken;
    @Value("${twilio.whatsapp-from-number}")
    private String twilioWhatsAppFromNumber;*/

    /**
     * Inicializa el SDK de Twilio una sola vez cuando el servicio se crea.
     */
    @PostConstruct
    public void initTwilio() {
        //Twilio.init(twilioAccountSid, twilioAuthToken);
    }

    @Override
    @Async
    public void sendVerificationCode(String email, String phoneNumber, String code) {
        // Enviar correo si se proporciona un email
        if (email != null && !email.isBlank()) {
            sendEmail(email, code);
        }

        // Enviar WhatsApp si se proporciona un número
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            sendWhatsAppVerification(phoneNumber, code);
        }
    }

    private void sendEmail(String email, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(fromEmail, senderName);
            helper.setTo(email);
            helper.setSubject("Tu Código de Acceso para AlaModaPeru");
            helper.setText(buildHtmlEmail(code), true);
            log.info("Enviando correo de verificación a: {}", email);
            mailSender.send(mimeMessage);
            log.info("Correo enviado exitosamente a: {}", email);
        } catch (Exception e) {
            log.error("Error al enviar email a [{}]: {}", email, e.getMessage());
        }
    }

    private void sendWhatsAppVerification(String userPhoneNumber, String code) {
        String formattedToNumber = "whatsapp:" + formatPhoneNumberForPeru(userPhoneNumber);
        // IMPORTANTE: Este texto debe coincidir con una plantilla aprobada por Meta.
        String messageBody = "Tu código de acceso para AlaModaPeru es " + code + ". Este código expira en 5 minutos.";

        try {
        //    log.info("Intentando enviar WhatsApp a [{}]. Desde [{}].", formattedToNumber, twilioWhatsAppFromNumber);
        /*    Message.creator(
                    new PhoneNumber(formattedToNumber),
                    new PhoneNumber(twilioWhatsAppFromNumber),
                    messageBody
            ).create();*/
            log.info("Mensaje de WhatsApp enviado exitosamente a: {}", formattedToNumber);
        } catch (Exception e) {
            log.error("Error al enviar WhatsApp a [{}]: {}", formattedToNumber, e.getMessage());
        }
    }

    private String formatPhoneNumberForPeru(String phone) {
        String cleanedPhone = phone.replaceAll("[^0-9]", "");
        if (cleanedPhone.length() == 9) { // 987654321
            return "+51" + cleanedPhone;
        }
        if (cleanedPhone.startsWith("51") && cleanedPhone.length() == 11) { // 51987654321
            return "+" + cleanedPhone;
        }
        if (phone.startsWith("+51") && phone.length() == 12) { // +51987654321
            return phone;
        }
        log.warn("El número {} no pudo ser formateado al estándar E.164. Se intentará usar como está.", phone);
        return phone; // Fallback
    }

    private String buildHtmlEmail(String code) {
        return "<div style='font-family:Arial,sans-serif;...'>" + code + "</div>"; // Tu HTML aquí
    }
}