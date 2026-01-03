package com.example.drawing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drawing.entity.LotteryItem;
import com.example.drawing.entity.LotteryItemState;

public interface LotteryItemStateRepository extends JpaRepository<LotteryItemState, Long> {

	Optional<LotteryItemState> findByLotteryItem(LotteryItem lotteryItem);
}
