package pillihuaman.com.pe.security.entity.permission;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pillihuaman.com.pe.security.Help.AuditEntity;

@Data
@Document(collection = "permission")
public class Permission {
    @Id
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("pageId")
    private ObjectId pageId;
    private AuditEntity audit;
}
