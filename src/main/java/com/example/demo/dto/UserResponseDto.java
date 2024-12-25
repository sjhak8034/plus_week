package com.example.demo.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserResponseDto {
    private final Long id;


//    public boolean testEquals(Object o) {
//        if (this == o) return true; // 오브젝트(주소까지) 같으면 true
//        if (o == null || getClass() != o.getClass()) return false; // null 이거나 오브젝트(타입)가 다르고 값도 다르면
//        UserResponseDto thisDto = (UserResponseDto) o;
//        return Objects.equals(id, thisDto.id); // 값을 비교해서 같으면 true;
//    }
}
