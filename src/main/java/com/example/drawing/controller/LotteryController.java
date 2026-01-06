package com.example.drawing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.repository.LotteryRepository;
import com.example.drawing.service.LotteryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lotteries")
public class LotteryController {

	private final LotteryRepository lotteryRepository;
	private final LotteryService lotteryService;

	@GetMapping("/{id}")
	public String showLottery(@PathVariable Long id, Model model) {
		Lottery lottery = lotteryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("くじが存在しません"));

		model.addAttribute("lottery", lottery);
		return "lottery/draw";
	}

	@PostMapping("/{id}/draw")
	public String draw(@PathVariable Long id, Model model) {
		Lottery lottery = lotteryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("くじが存在しません"));

		LotteryItem result = lotteryService.draw(lottery);

		model.addAttribute("lottery", lottery);
		model.addAttribute("result", result);

		return "lottery/draw";
	}

}
