package com.example.drawing.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.drawing.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RepositoryTestRunner implements CommandLineRunner {

	private final UserRepository userRepository;

	@Override
	public void run(String... args) {
		System.out.println("User count = " + userRepository.count());
	}

}
