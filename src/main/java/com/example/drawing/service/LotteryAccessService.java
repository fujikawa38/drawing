package com.example.drawing.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.entity.User;
import com.example.drawing.repository.LotteryItemRepository;
import com.example.drawing.repository.LotteryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotteryAccessService {

	private final LotteryRepository lotteryRepository;
	private final LotteryItemRepository itemRepository;

	public Lottery getLotteryForUser(Long lotteryId, User user) {
		Lottery lottery = lotteryRepository.findById(lotteryId)
				.orElseThrow(() -> new IllegalArgumentException("くじが存在しません"));

		if (!lottery.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("アクセス権がありません");
		}

		return lottery;
	}

	public LotteryItem getItemForLottery(Long itemId, Lottery lottery) {
		LotteryItem item = itemRepository.findById(itemId).orElseThrow();

		if (!item.getLottery().getId().equals(lottery.getId())) {
			throw new IllegalArgumentException("不正なアクセス");
		}

		return item;
	}

}
