package pillihuaman.com.pe.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
public class ThymeleafConfig {





    @Bean(name = "customTemplateEngine")
    public SpringTemplateEngine templateEngine() {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            StringTemplateResolver resolver = new StringTemplateResolver();
            resolver.setTemplateMode("HTML");
            resolver.setCacheable(false);
            engine.setTemplateResolver(resolver);
            return engine;
        }



}
