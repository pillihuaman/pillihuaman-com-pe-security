package pillihuaman.com.pe.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "email")
public class EmailTemplatesConfig {
    private Map<String, String> templates;
}