package pillihuaman.com.pe.security.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pillihuaman.com.pe.security.entity.notify.ChannelType;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    @NotNull
    private ChannelType channel;
    @NotBlank
    private String recipient;
    @NotBlank
    private String templateId;
    private Map<String, Object> payload;
    @Builder.Default
    private String language = "es";
}

