package com.example.drawing.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.drawing.entity.LotteryItem;
import com.example.drawing.repository.LotteryRepository;
import com.example.drawing.service.LotteryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LotteryTestRunner implements CommandLineRunner {

	private final LotteryService lotteryService;
	private final LotteryRepository lotteryRepository;

	@Override
	public void run(String... args) {
		lotteryRepository.findById(1L).ifPresentOrElse(
				lottery -> {
					LotteryItem result = lotteryService.draw(lottery);
					System.out.println("当選結果：" + result.getName());
				},
				() -> System.out.println("ID:1のLotteryが見つかりません。先にデータを作成してください"));
	}

}
