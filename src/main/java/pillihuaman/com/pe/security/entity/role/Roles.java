package pillihuaman.com.pe.security.entity.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pillihuaman.com.pe.security.Help.AuditEntity;
import pillihuaman.com.pe.security.entity.user.User;


import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Document(collection = "roles")
public class Roles implements Serializable {
    @Id
    private ObjectId id;
    private String name;
    private boolean active;
    @Field("description")
    private String description;
    private List<ObjectId> permissionIds;
    @DBRef
    private List<User> assignedUsers;
    private AuditEntity audit;

    public Roles() {

    }

}




