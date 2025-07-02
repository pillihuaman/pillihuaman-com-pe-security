package pillihuaman.com.pe.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pillihuaman.com.pe.lib.domain.TenantInterceptor;
import pillihuaman.com.pe.lib.domain.TenantResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TenantResolver tenantResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor(tenantResolver));
    }
}
