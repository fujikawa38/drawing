package com.example.drawing.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

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
public class LotteryService {

	private final LotteryItemRepository itemRepository;
	private final LotteryItemStateRepository stateRepository;

	@Transactional
	public LotteryItem draw(Lottery lottery) {
		List<LotteryItem> items = itemRepository.findByLotteryAndEnabledTrue(lottery);

		if (items.isEmpty()) {
			throw new IllegalStateException("抽選できる項目がありません");
		}

		List<LotteryItemState> states = stateRepository.findByLotteryItem_Lottery(lottery);

		Map<Long, LotteryItemState> stateMap = states.stream()
				.collect(Collectors.toMap(s -> s.getLotteryItem().getId(), s -> s));

		List<LotteryItem> availableItems = items.stream().filter(item -> isAvailable(item, lottery, stateMap)).toList();

		if (availableItems.isEmpty()) {

			stateMap.values().forEach(LotteryItemState::resetExclude);

			availableItems = items.stream().filter(item -> isAvailable(item, lottery, stateMap)).toList();
		}

		if (availableItems.isEmpty()) {
			throw new IllegalStateException("抽選可能な項目が存在しません");
		}

		LotteryItem selected = availableItems.get(new Random().nextInt(availableItems.size()));

		updateStates(lottery, selected, items, stateMap);

		return selected;
	}

	private boolean isAvailable(LotteryItem item, Lottery lottery, Map<Long, LotteryItemState> stateMap) {

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

	private void updateStates(Lottery lottery, LotteryItem selected, List<LotteryItem> allItems,
			Map<Long, LotteryItemState> stateMap) {

		for (LotteryItem item : allItems) {
			LotteryItemState state = stateMap.computeIfAbsent(item.getId(), id -> createState(item));

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
