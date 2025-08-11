package pillihuaman.com.pe.security.repository;

import pillihuaman.com.pe.security.entity.notify.ChannelType;
import pillihuaman.com.pe.security.entity.notify.NotificationTemplate;

import java.util.Optional;

public interface NotificationTemplateRepository extends BaseMongoRepository<NotificationTemplate> {
    Optional<NotificationTemplate> findByTemplateIdAndChannelAndLanguage(String templateId, ChannelType channel, String language);


}