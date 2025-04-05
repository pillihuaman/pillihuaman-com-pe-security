package pillihuaman.com.pe.security.Help;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@Setter
@Getter
public class AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @BsonProperty(value = "_id")
    private ObjectId id;
    private ObjectId codUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX")
    private Date dateRegister;

    private ObjectId codUserUpdate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX")
    private Date dateUpdate;
    private String mail;
    private String mailUpdate;
    public AuditEntity() {}
}
