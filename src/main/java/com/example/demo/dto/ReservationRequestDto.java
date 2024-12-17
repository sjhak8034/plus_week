package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReservationRequestDto {
    private final Long itemId;
    private final Long userId;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
}
