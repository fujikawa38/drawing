package com.example.drawing.exception;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDenied(AccessDeniedException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error/403";
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public String handleNotFound(EntityNotFoundException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error/404";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
		model.addAttribute("message", ex.getMessage());
		return "error/400";
	}

	@ExceptionHandler(Exception.class)
	public String handleSystemError(Exception ex, Model model) {
		logger.error("System error occurred", ex);
		model.addAttribute("message", "システムエラーが発生しました。時間をおいて再度お試しください。");
		return "error/500";
	}

}
