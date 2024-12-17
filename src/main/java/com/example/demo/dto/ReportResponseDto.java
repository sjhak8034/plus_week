package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ReportResponseDto {
    private final List<Long> userIds;
}
