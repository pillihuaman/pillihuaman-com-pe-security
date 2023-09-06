package pillihuaman.com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import pillihuaman.com.lib.exception.CustomRestExceptionHandlerGeneric;

//@EnableAsync
//@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
//@EnableScheduling
//@Import(CustomRestExceptionHandlerGeneric.class)
//@SpringBootApplication(scanBasePackages = {"pillihuaman.com.basebd.config","pillihuaman.com.basebd","pillihuaman.com.security","pillihuaman.com.basebd.user.dao"})
//@ComponentScan(basePackages = {"pillihuaman.com.lib"})


@SpringBootApplication(scanBasePackages = {"pillihuaman.com.basebd.config", "pillihuaman.com.basebd", "pillihuaman.com.security"})
@Import(CustomRestExceptionHandlerGeneric.class)
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
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
}
