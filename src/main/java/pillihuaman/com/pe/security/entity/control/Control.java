package pillihuaman.com.pe.security.entity.control;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pillihuaman.com.pe.security.Help.AuditEntity;
import pillihuaman.com.pe.security.entity.role.Roles;
import pillihuaman.com.pe.security.entity.user.User;


import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Document(collection = "control")
@Builder
@NoArgsConstructor
@Data
public class Control implements    Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private ObjectId id;
    private AuditEntity auditEntity;
    private String idCode;
    private String description;
    private String icono;
    private String iconClass;
    private int status;
    private String styleClass;
    private User user;
    private String text;
    private List<Roles> rol;

}
