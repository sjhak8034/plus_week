package com.example.demo.dto;

import com.example.demo.entity.ReservationStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class UpdateReservationResponseDto {
    private final Long id;
    private final ReservationStatus status;
}
