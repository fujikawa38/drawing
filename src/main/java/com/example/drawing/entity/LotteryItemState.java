package com.example.drawing.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lottery_item_states")
@Getter
@Setter
public class LotteryItemState {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lottery_item_id", nullable = false, unique = true)
	private LotteryItem lotteryItem;

	@Column(name = "remaining_count", nullable = false)
	private int remainingCount = 0;

	@Column(name = "excluded_until")
	private LocalDateTime excludedUntil;
}
