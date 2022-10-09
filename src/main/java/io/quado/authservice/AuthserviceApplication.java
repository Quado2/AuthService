package io.quado.authservice;

import io.quado.authservice.domain.AppUser;
import io.quado.authservice.domain.Role;
import io.quado.authservice.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class AuthserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserviceApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run(AppUserService userService){
		return args -> {
			userService.saveRole(Role.builder().name("ROLE_USER").build());
			userService.saveRole(Role.builder().name("ROLE_MANAGER").build());
			userService.saveRole(Role.builder().name("ROLE_ADMIN").build());
			userService.saveRole(Role.builder().name("ROLE_SUPER_ADMIN").build());


			userService.saveUser(AppUser.builder().name("Chukwu Ebuka").username("ebuka").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().name("Edozie Okolo").username("edozie").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().name("Ndubuisi Nnaemeka").username("ndu").password("1234").roles(new ArrayList<>()).build());
			userService.saveUser(AppUser.builder().name("Okolo Ebube").username("bube").password("1234").roles(new ArrayList<>()).build());

			userService.saveRoleToUser("ebuka","ROLE_USER");
			userService.saveRoleToUser("edozie","ROLE_MANAGER");
			userService.saveRoleToUser("ebuka","ROLE_MANAGER");
			userService.saveRoleToUser("ebuka","ROLE_ADMIN");
			userService.saveRoleToUser("ndu","ROLE_ADMIN");
			userService.saveRoleToUser("ndu","ROLE_SUPER_ADMIN");
			userService.saveRoleToUser("ndu","ROLE_USER");
		};
	}
}
