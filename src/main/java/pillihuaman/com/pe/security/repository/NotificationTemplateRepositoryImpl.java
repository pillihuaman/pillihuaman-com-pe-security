package pillihuaman.com.pe.security.repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.springframework.stereotype.Component;
import pillihuaman.com.pe.security.AzureAbstractMongoRepositoryImpl;
import pillihuaman.com.pe.security.Help.Constants;
import pillihuaman.com.pe.security.entity.notify.ChannelType;
import pillihuaman.com.pe.security.entity.notify.NotificationTemplate;

import java.util.Optional;


@Component
    public class NotificationTemplateRepositoryImpl extends AzureAbstractMongoRepositoryImpl<NotificationTemplate> implements NotificationTemplateRepository {

         NotificationTemplateRepositoryImpl() {
            DS_WRITE = Constants.DW;
            // DS_READ = Constants.DR;
            COLLECTION = Constants.COLLECTION_NOTIFICATION_TEMPLATE;
        }



    @Override
    public Class<NotificationTemplate> provideEntityClass() {
        return NotificationTemplate.class;
    }

    @Override
    public Optional<NotificationTemplate> findByTemplateIdAndChannelAndLanguage(String templateId, ChannelType channel, String language) {
        var filter = Filters.and(
                Filters.eq("templateId", templateId),
                Filters.eq("channel", channel),
                Filters.eq("language", language)
        );

        MongoCollection<NotificationTemplate> collection = getCollection(COLLECTION, NotificationTemplate.class);
        NotificationTemplate result = collection.find(filter, NotificationTemplate.class).first();

        return Optional.ofNullable(result);
    }



    @Override
    public NotificationTemplate save(NotificationTemplate document) {
        return super.save(document);
    }
}
