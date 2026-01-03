package com.example.drawing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.drawing.entity.Lottery;
import com.example.drawing.entity.User;

public interface LotteryRepository extends JpaRepository<Lottery, Long> {

	List<Lottery> findByUser(User user);

}
