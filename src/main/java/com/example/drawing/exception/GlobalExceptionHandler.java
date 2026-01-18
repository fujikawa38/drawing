package com.example.drawing.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDenied(AccessDeniedException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error/403";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error/404";
	}

}
