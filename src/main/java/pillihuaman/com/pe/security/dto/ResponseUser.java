package pillihuaman.com.pe.security.dto;

import lombok.*;
import org.bson.types.ObjectId;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    private ObjectId id;
    private String alias;
    private ObjectId idSystem;
    private String mail;
    private String mobilPhone;
    private String user;
    private String username;
    private int enabled;
    private ObjectId idRol;

}

