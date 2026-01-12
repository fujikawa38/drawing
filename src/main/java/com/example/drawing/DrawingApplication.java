package com.example.drawing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DrawingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrawingApplication.class, args);
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("12345678"));
	}

}
