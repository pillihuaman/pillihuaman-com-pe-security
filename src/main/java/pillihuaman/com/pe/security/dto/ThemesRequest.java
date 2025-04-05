package pillihuaman.com.pe.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


