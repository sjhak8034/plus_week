package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.UpdateReservationResponseDto;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.Role;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReservationController.class)
class ReservationControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ReservationService reservationService;
    @InjectMocks
    ReservationController reservationController;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createReservation() throws Exception {
        //given
        Long itemId = 1L;
        Long userId = 1L;
        LocalDateTime startAt = LocalDateTime.of(2020, 1, 1, 1, 1,0);
        LocalDateTime endAt = LocalDateTime.of(2021, 1, 1, 1, 1,0);

        String nickname = "nickname";
        String itemName = "itemName";

        ReservationRequestDto requestDto = new ReservationRequestDto(itemId,userId,startAt,endAt);
        ReservationResponseDto responseDto  = new ReservationResponseDto(itemId,nickname,itemName,startAt,endAt);
        Authentication authentication = new Authentication(userId,Role.USER);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(GlobalConstants.USER_AUTH, authentication);
        //when
        when(reservationService.createReservation(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(responseDto);
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .session(mockHttpSession)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.nickname").value(responseDto.getNickname()))
                .andExpect(jsonPath("$.itemName").value(responseDto.getItemName()))
                .andExpect(jsonPath("$.startAt").value(responseDto.getStartAt().format(formatter)))
                .andExpect(jsonPath("$.endAt").value(responseDto.getEndAt().format(formatter)));

    }

    @Test
    void updateReservation() throws Exception {
        // given
        Long userId = 1L;
        Authentication authentication = new Authentication(userId,Role.USER);
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(GlobalConstants.USER_AUTH, authentication );
        Long reservationId = 1L;

        ReservationStatus reservationStatus = ReservationStatus.APPROVED;
        UpdateReservationResponseDto responseDto  = new UpdateReservationResponseDto(reservationId, ReservationStatus.APPROVED);
        //when
        when(reservationService.updateReservationStatus(anyLong(),any(ReservationStatus.class))).thenReturn(responseDto);

        //then
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/reservations/1/update-status")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationStatus)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(responseDto.getId()))
                        .andExpect(jsonPath("$.status").value(responseDto.getStatus().toString()));
    }

    @Test
    void findAll() throws Exception {
        // given
        Long userId = 1L;
        Authentication authentication = new Authentication(userId,Role.USER);
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(GlobalConstants.USER_AUTH, authentication );
        List<ReservationResponseDto> responseDtoList = new ArrayList<>();
        // when
        when(reservationService.getReservations()).thenReturn(responseDtoList);
        //then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/reservations")
                .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(responseDtoList));
    }

    @Test
    void searchAll() throws Exception {
        //given
        Long userId = 1L;
        Long itemId = 1L;
        Authentication authentication = new Authentication(userId,Role.USER);
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(GlobalConstants.USER_AUTH, authentication );
        List<ReservationResponseDto> responseDtoList = new ArrayList<>();
        //when
        when(reservationService.searchAndConvertReservations(anyLong(),anyLong())).thenReturn(responseDtoList);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/reservations/search")
                        .queryParam("userId",userId.toString()).queryParam("itemId",itemId.toString())
                        .session(mockHttpSession))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(responseDtoList));
    }
}