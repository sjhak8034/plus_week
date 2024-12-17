package com.example.demo.dto;

import com.example.demo.entity.ReservationStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateReservationResponseDto {
    private final Long id;
    private final ReservationStatus status;
}
