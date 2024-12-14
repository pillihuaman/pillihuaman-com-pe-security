package pillihuaman.com.pe.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import pillihuaman.com.pe.lib.exception.CustomRestExceptionHandlerGeneric;


//@EnableAsync
//@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
//@EnableScheduling
//@Import(CustomRestExceptionHandlerGeneric.class)
//@SpringBootApplication(scanBasePackages = {"pillihuaman.com.basebd.config","pillihuaman.com.basebd","pillihuaman.com.security","pillihuaman.com.basebd.user.dao"})
//@ComponentScan(basePackages = {"pillihuaman.com.lib"})



@SpringBootApplication(scanBasePackages = {"pillihuaman.com.pe.basebd.config",
        "pillihuaman.com.pe.basebd", "pillihuaman.com.pe.security"})

@Import(CustomRestExceptionHandlerGeneric.class)
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SecurityApplication.class);
        // Configura el puerto directamente
        app.setDefaultProperties(java.util.Map.of("server.port", "8085"));
        app.run(args);
    }
}
/*
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}*/

