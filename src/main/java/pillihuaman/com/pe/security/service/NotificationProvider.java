package pillihuaman.com.pe.security.service;


import pillihuaman.com.pe.security.repository.GenericNotificationServiceImpl.NotificationDetails;

public interface NotificationProvider {
    void send(NotificationDetails details);
}