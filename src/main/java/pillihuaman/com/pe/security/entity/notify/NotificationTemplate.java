package pillihuaman.com.pe.security.entity.notify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor // <- Constructor vacío para Mongo
@AllArgsConstructor // <- Constructor con todos los campos
@Document(collection = "notification_templates")
@CompoundIndex(name = "template_idx", def = "{'templateId': 1, 'channel': 1, 'language': 1}", unique = true)
public class NotificationTemplate {
    @Id
    private String id;
    private String templateId; // Ej: "VERIFICATION_CODE", "WELCOME_EMAIL"
    private String description;
    private String subject;
    private String content; // HTML o texto plano
    private ChannelType channel;
    private String language; // ej: "es", "en"
}
