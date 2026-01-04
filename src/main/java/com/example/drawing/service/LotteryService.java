package com.example.drawing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.entity.LotteryItemState;
import com.example.drawing.repository.LotteryItemRepository;
import com.example.drawing.repository.LotteryItemStateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LotteryService {

	private final LotteryItemRepository itemRepository;
	private final LotteryItemStateRepository stateRepository;

	public LotteryItem draw(Lottery lottery) {
		List<LotteryItem> items = itemRepository.findByLotteryAndEnabledTrue(lottery);

		if (items.isEmpty()) {
			throw new IllegalStateException("抽選できる項目がありません");
		}

		List<LotteryItem> availableItems = items.stream().filter(item -> isAvailable(item, lottery)).toList();

		if (availableItems.isEmpty()) {
			throw new IllegalStateException("すべての項目が抽選の対象外です");
		}

		LotteryItem selected = availableItems.get(new Random().nextInt(availableItems.size()));

		updateStates(lottery, selected, items);

		return selected;
	}

	private boolean isAvailable(LotteryItem item, Lottery lottery) {

		LotteryItemState state = stateRepository.findByLotteryItem(item).orElseGet(() -> createState(item));

		return switch (lottery.getExcludeType()) {
		case NONE -> true;
		case COUNT -> state.getRemainingCount() == 0;
		case DATE -> state.getExcludedUntil() == null || state.getExcludedUntil().isBefore(LocalDateTime.now());
		};
	}

	private LotteryItemState createState(LotteryItem item) {
		LotteryItemState state = new LotteryItemState();
		state.setLotteryItem(item);
		return stateRepository.save(state);
	}

	private void updateStates(Lottery lottery, LotteryItem selected, List<LotteryItem> allItems) {

		for (LotteryItem item : allItems) {
			LotteryItemState state = stateRepository.findByLotteryItem(item).orElseGet(() -> createState(item));

			switch (lottery.getExcludeType()) {
			case NONE -> {
			}
			case COUNT -> {
				if (item.equals(selected)) {
					state.setRemainingCount(lottery.getExcludeCount());
				} else if (state.getRemainingCount() > 0) {
					state.setRemainingCount(state.getRemainingCount() - 1);
				}
			}
			case DATE -> {
				if (item.equals(selected)) {
					state.setExcludedUntil(LocalDateTime.now().plusDays(lottery.getExcludeDays()));
				}
			}

			}
		}
	}

}
