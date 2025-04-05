package pillihuaman.com.pe.security;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootApplication(scanBasePackages = {"pillihuaman.com.pe.security", "pillihuaman.com.pe.security.repository"})
@ComponentScan(basePackages = {"pillihuaman.com.pe"})
@EnableMongoRepositories(basePackages = "pillihuaman.com.pe.security.repository")

@Import(CustomRestExceptionHandlerGeneric.class)
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SecurityApplication.class);
        app.setDefaultProperties(java.util.Map.of("server.port", "8085"));
        app.run(args);
    }

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    }

}
