package com.example.drawing.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.drawing.entity.ExcludeType;
import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.entity.User;
import com.example.drawing.repository.LotteryRepository;
import com.example.drawing.repository.UserRepository;
import com.example.drawing.service.LotteryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lotteries")
public class LotteryController {

	private final LotteryRepository lotteryRepository;
	private final UserRepository userRepository;
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

	@GetMapping
	public String list(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String username = auth.getName();
		User user = userRepository.findByUsername(username).orElseThrow();

		List<Lottery> lotteries = lotteryRepository.findByUser(user);

		model.addAttribute("lotteries", lotteries);
		return "lottery/list";
	}

	@GetMapping("/new")
	public String newLottery(Model model) {
		model.addAttribute("lottery", new Lottery());
		model.addAttribute("excludeTypes", ExcludeType.values());

		return "lottery/new";
	}

	@PostMapping
	public String create(@ModelAttribute Lottery lottery) {
		User user = new User();
		user.setId(1L);
		lottery.setUser(user);
		lotteryRepository.save(lottery);
		return "redirect:/lotteries";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Lottery lottery = lotteryRepository.findById(id).orElseThrow();
		model.addAttribute("lottery", lottery);
		return "lottery/edit";
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Lottery form) {
		Lottery lottery = lotteryRepository.findById(id).orElseThrow();

		lottery.setTitle(form.getTitle());
		lottery.setExcludeType(form.getExcludeType());
		lottery.setExcludeCount(form.getExcludeCount());
		lottery.setExcludeDays(form.getExcludeDays());

		lotteryRepository.save(lottery);
		return "redirect:/lotteries";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		lotteryRepository.deleteById(id);
		return "redirect:/lotteries";
	}

}
