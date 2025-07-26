package pillihuaman.com.pe.security.service;

public interface NotificationService {

    /**
     * Envía un código de verificación tanto por email como por SMS.
     * @param email La dirección de correo del destinatario.
     * @param phoneNumber El número de teléfono del destinatario (formato E.164, ej: +51987654321).
     * @param code El código de 4 dígitos a enviar.
     */
    void sendVerificationCode(String email, String phoneNumber, String code);
}