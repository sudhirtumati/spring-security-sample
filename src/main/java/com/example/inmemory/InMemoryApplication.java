package com.example.inmemory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
public class InMemoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(InMemoryApplication.class, args);
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager();
	}

	@Bean
	InitializingBean initializer(UserDetailsManager userDetailsManager) {
		return () -> {
			userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("user1").password("password1").roles("user").build());
			userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("user2").password("password2").roles("user").build());
			userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("admin1").password("password1").roles("admin").build());
			userDetailsManager.createUser(User.withDefaultPasswordEncoder().username("admin2").password("password2").roles("admin").build());
		};
	}
}

@RestController
class GreetingController {

	@GetMapping("/greetings")
	String getGreetings(Principal principal) {
		return "Hello, " + principal.getName();
	}
}

@Configuration
@EnableWebSecurity
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic();
		http.authorizeRequests().anyRequest().authenticated();
	}
}