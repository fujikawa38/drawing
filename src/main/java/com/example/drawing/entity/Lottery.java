package com.example.drawing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "lotteries")
@Getter
@Setter
public class Lottery extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotBlank(message = "タイトルは必須です")
	@Size(max = 50, message = "タイトルは50文字以内で入力してください")
	@Column(nullable = false)
	private String title;

	private String description;

	@NotNull(message = "除外タイプを選択してください")
	@Enumerated(EnumType.STRING)
	@Column(name = "exclude_type", nullable = false)
	private ExcludeType excludeType = ExcludeType.NONE;

	@Min(value = 0, message = "除外数は0以上で入力してください")
	@Column(name = "exclude_count")
	private Integer excludeCount;

	@Min(value = 0, message = "除外日数は0以上で入力してください")
	@Column(name = "exclude_days")
	private Integer excludeDays;

}
