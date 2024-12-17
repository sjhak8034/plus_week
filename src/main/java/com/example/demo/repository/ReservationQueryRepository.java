package com.example.demo.repository;

import com.example.demo.entity.Reservation;

import java.util.List;

public interface ReservationQueryRepository {
    List<Reservation> findByUserIdAndItemId(Long userId, Long itemId);
}
