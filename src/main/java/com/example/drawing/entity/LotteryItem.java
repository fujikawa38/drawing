package com.example.drawing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lottery_items")
@Getter
@Setter
public class LotteryItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lottery_id", nullable = false)
	private Lottery lottery;

	@NotBlank(message = "項目名は必須です")
	@Size(max = 30, message = "30文字以内で入力してください")
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private boolean enabled = true;

}
