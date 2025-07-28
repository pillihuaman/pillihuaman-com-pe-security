package pillihuaman.com.pe.security.entity.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pillihuaman.com.pe.lib.common.AuditEntity;
import pillihuaman.com.pe.security.entity.permission.Permission;
import pillihuaman.com.pe.security.entity.user.User;


import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@Document(collection = "page")
public class Page implements Serializable {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private String url;
    @Field("name")
    private String name;
    @Field("description")
    private String description;
    @Field("menuId")
    private ObjectId menuId;
    private List<Permission> permissions;
    // Additional properties
    private boolean isPublished;
    private String metaKeywords;
    private String metaDescription;
    private List<User> authorizedUsers;
    private AuditEntity audit;
}