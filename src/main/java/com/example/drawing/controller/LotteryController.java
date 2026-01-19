package com.example.drawing.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.example.drawing.security.LoginUser;
import com.example.drawing.service.LotteryAccessService;
import com.example.drawing.service.LotteryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lotteries")
public class LotteryController {

	private final LotteryRepository lotteryRepository;
	private final LotteryService lotteryService;
	private final LotteryAccessService lotteryAccessService;

	@GetMapping("/{id}")
	public String showLottery(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(id, loginUser.getUser());

		model.addAttribute("lottery", lottery);
		return "lottery/draw";
	}

	@PostMapping("/{id}/draw")
	public String draw(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(id, loginUser.getUser());

		LotteryItem result = lotteryService.draw(lottery);

		model.addAttribute("lottery", lottery);
		model.addAttribute("result", result);

		return "lottery/draw";
	}

	@GetMapping
	public String list(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		User user = loginUser.getUser();
		List<Lottery> lotteries = lotteryRepository.findByUser(user);
		model.addAttribute("lotteries", lotteries);
		return "lottery/list";
	}

	@GetMapping("/new")
	public String newLottery(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		model.addAttribute("lottery", new Lottery());
		model.addAttribute("excludeTypes", ExcludeType.values());

		return "lottery/new";
	}

	@PostMapping
	public String create(@AuthenticationPrincipal LoginUser loginUser, @Valid @ModelAttribute Lottery lottery,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("excludeTypes", ExcludeType.values());
			return "lottery/new";
		}
		lottery.setUser(loginUser.getUser());
		lotteryRepository.save(lottery);
		return "redirect:/lotteries";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(id, loginUser.getUser());

		model.addAttribute("lottery", lottery);
		return "lottery/edit";
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser,
			@Valid @ModelAttribute Lottery form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("lottery", form);
			model.addAttribute("excludeTypes", ExcludeType.values());
			return "lottery/edit";
		}
		Lottery lottery = lotteryAccessService.getLotteryForUser(id, loginUser.getUser());

		lottery.setTitle(form.getTitle());
		lottery.setExcludeType(form.getExcludeType());
		lottery.setExcludeCount(form.getExcludeCount());
		lottery.setExcludeDays(form.getExcludeDays());

		lotteryRepository.save(lottery);
		return "redirect:/lotteries";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id, @AuthenticationPrincipal LoginUser loginUser) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(id, loginUser.getUser());

		lotteryRepository.delete(lottery);
		return "redirect:/lotteries";
	}

}
