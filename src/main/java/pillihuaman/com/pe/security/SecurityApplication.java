package pillihuaman.com.pe.security;

import com.mongodb.MongoClientSettings;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan; // <-- AÑADIR IMPORT
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootApplication(scanBasePackages = {"pillihuaman.com.pe.security"})
@ComponentScan(basePackages = {"pillihuaman.com.pe"})
@EnableMongoRepositories(basePackages = "pillihuaman.com.pe.security.repository")
@Import(CustomRestExceptionHandlerGeneric.class)
@ConfigurationPropertiesScan("pillihuaman.com.pe.security") // <-- AÑADIR ESTA ANOTACIÓN
public class SecurityApplication {

    public static void main(String[] args) {
        // Es una mejor práctica definir el puerto en application.properties
        var context = SpringApplication.run(SecurityApplication.class, args);
        System.out.println("Application 'security' started successfully!");

        String port = context.getEnvironment().getProperty("local.server.port");
        System.out.println("✅ Security app running on port: " + port);
    }

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );
    }
}
