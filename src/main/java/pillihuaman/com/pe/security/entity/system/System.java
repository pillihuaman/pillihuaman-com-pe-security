package pillihuaman.com.pe.security.entity.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.security.Help.AuditEntity;
import pillihuaman.com.pe.security.entity.menu.Menu;


import java.io.Serializable;
import java.util.List;


@Builder
@AllArgsConstructor
@Data
@Document(collection = "system")
public class System implements Serializable {
    @Id
    private ObjectId id;
    private String name;
    private String version;
    private List<Menu> menus;
    private String timezone;
    private boolean isActive;
    private String contactEmail;
    private String supportPhone;
    private AuditEntity audit;

    public System() {

    }

    public org.bson.Document toDocument() {
        org.bson.Document document = new org.bson.Document();
        document.append("name", this.name);
        document.append("version", this.version);
        document.append("menus", this.menus);
        document.append("timezone", this.timezone);
        document.append("isActive", this.isActive);
        document.append("contactEmail", this.contactEmail);
        document.append("supportPhone", this.supportPhone);
        return document;
    }
}