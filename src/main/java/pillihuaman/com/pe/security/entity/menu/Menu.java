package pillihuaman.com.pe.security.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import pillihuaman.com.pe.security.entity.page.Page;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Document(collection = "menu")
@Builder
@Data
public class Menu implements Serializable {
    @Id
    private ObjectId id;
    private String name;
    private System system;
    @Field("systemId")
    private ObjectId systemId;
    private List<Page> pages;
}
