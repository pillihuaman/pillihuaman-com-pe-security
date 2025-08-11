package pillihuaman.com.pe.security.service;

import pillihuaman.com.pe.security.dto.NotificationRequestDTO;

public interface GenericNotificationService {
    void dispatchNotification(NotificationRequestDTO request);
}