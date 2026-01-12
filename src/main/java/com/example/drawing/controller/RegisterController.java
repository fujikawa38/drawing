package com.example.drawing.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.drawing.entity.User;
import com.example.drawing.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user, Model model) {
		if (userRepository.existsByUsername(user.getUsername())) {
			model.addAttribute("error", "すでに使われているユーザー名です");
			return "register";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "redirect:/login";
	}
}
