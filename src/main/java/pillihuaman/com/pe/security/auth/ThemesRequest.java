package pillihuaman.com.pe.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.security.user.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThemesRequest {

    private String primary;
    private String secondary;
    private String headerBackground;
    private String buttonColor;
}


