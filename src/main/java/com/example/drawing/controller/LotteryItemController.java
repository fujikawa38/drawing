package com.example.drawing.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.repository.LotteryItemRepository;
import com.example.drawing.repository.LotteryRepository;
import com.example.drawing.security.LoginUser;
import com.example.drawing.service.LotteryAccessService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/lotteries/{lotteryId}/items")
public class LotteryItemController {

	private final LotteryRepository lotteryRepository;
	private final LotteryItemRepository itemRepository;
	private final LotteryAccessService lotteryAccessService;

	@GetMapping
	public String list(@PathVariable Long lotteryId, @AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		List<LotteryItem> items = itemRepository.findByLotteryAndEnabledTrue(lottery);

		model.addAttribute("lottery", lottery);
		model.addAttribute("items", items);

		return "item/list";
	}

	@GetMapping("/new")
	public String newItem(@PathVariable Long lotteryId, @AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		LotteryItem item = new LotteryItem();
		item.setLottery(lottery);

		model.addAttribute("lottery", lottery);
		model.addAttribute("item", item);

		return "item/new";
	}

	@PostMapping
	public String create(@PathVariable Long lotteryId, @AuthenticationPrincipal LoginUser loginUser,
			@ModelAttribute LotteryItem item) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		item.setLottery(lottery);
		itemRepository.save(item);

		return "redirect:/lotteries/" + lotteryId + "/items";
	}

	@GetMapping("/{itemId}/edit")
	public String edit(@PathVariable Long lotteryId, @PathVariable Long itemId,
			@AuthenticationPrincipal LoginUser loginUser, Model model) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		LotteryItem item = lotteryAccessService.getItemForLottery(itemId, lottery);

		model.addAttribute("item", item);
		model.addAttribute("lotteryId", lotteryId);

		return "item/edit";
	}

	@PostMapping("/{itemId}")
	public String update(@PathVariable Long lotteryId, @PathVariable Long itemId,
			@AuthenticationPrincipal LoginUser loginUser, @ModelAttribute LotteryItem form) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		LotteryItem item = lotteryAccessService.getItemForLottery(itemId, lottery);

		item.setName(form.getName());
		item.setEnabled(form.isEnabled());

		itemRepository.save(item);

		return "redirect:/lotteries/" + lotteryId + "/items";
	}

	@PostMapping("/{itemId}/delete")
	public String delete(@PathVariable Long lotteryId, @PathVariable Long itemId,
			@AuthenticationPrincipal LoginUser loginUser) {
		Lottery lottery = lotteryAccessService.getLotteryForUser(lotteryId, loginUser.getUser());

		LotteryItem item = lotteryAccessService.getItemForLottery(itemId, lottery);

		itemRepository.delete(item);

		return "redirect:/lotteries/" + lotteryId + "/items";
	}

}
