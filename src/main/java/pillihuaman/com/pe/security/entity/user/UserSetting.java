package pillihuaman.com.pe.security.entity.user;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@Builder
@AllArgsConstructor

@Document(collection = "user_settings")
public class UserSetting {
    @Id
    private String id;

    @Field("primary")
    private String primary;

    @Field("headerColor")
    private String headerColor;

    @Field("secondary")
    private String secondary;
    @Field("headerBackground")
    private String headerBackground;
    @Field("buttonColor")
    private String buttonColor;

    public UserSetting() {

    }
}
