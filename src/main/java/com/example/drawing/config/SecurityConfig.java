package com.example.drawing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/login/**", "/register", "/css/**", "/js/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/lotteries", true)
						.permitAll())
				.logout(logout -> logout
						.logoutSuccessUrl("/login?logout"));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
