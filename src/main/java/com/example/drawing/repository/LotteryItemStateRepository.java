package com.example.drawing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;
import com.example.drawing.entity.LotteryItemState;

public interface LotteryItemStateRepository extends JpaRepository<LotteryItemState, Long> {

	Optional<LotteryItemState> findByLotteryItem(LotteryItem lotteryItem);

	List<LotteryItemState> findByLotteryItem_Lottery(Lottery lottery);
}
