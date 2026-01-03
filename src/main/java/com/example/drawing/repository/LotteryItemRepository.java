package com.example.drawing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.LotteryItem;

public interface LotteryItemRepository extends JpaRepository<LotteryItem, Long> {

	List<LotteryItem> findByLotteryAndEnabledTrue(Lottery lottery);
}
