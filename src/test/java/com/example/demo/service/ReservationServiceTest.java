package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.UpdateReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RentalLogService rentalLogService;
    @InjectMocks
    ReservationService reservationService;
    private Long itemId;
    private Long userId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String description;
    private User owner;
    private User manager;
    private Item item;
    private Reservation reservation;
    private User customer;
    private Long reservationId;

    @BeforeEach
    void setUp() {
        itemId = 1L;
        startAt = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        endAt = LocalDateTime.of(2024, 1, 2, 0, 0, 0);
        owner = new User("user", "owner@test", "owner", PasswordEncoder.encode("owner"));
        owner.setId(1L);
        manager = new User("user", "manager@test", "manager", PasswordEncoder.encode("manager"));
        manager.setId(2L);
        customer = new User("user", "customer@test", "customer", PasswordEncoder.encode("customer"));
        customer.setId(3L);
        item = new Item("item1", "item1desc", manager, owner);
        item.setId(1L);
        reservationId = 1L;
        reservation = new Reservation(item, customer, startAt, endAt);
        reservation.setId(reservationId);
    }

    @Test
    void createReservation() {
        ReservationResponseDto responseDto = new ReservationResponseDto(reservationId, customer.getNickname(), item.getName(), startAt, endAt);
        List<Reservation> empty = Collections.emptyList();

        when(reservationRepository.findConflictingReservations(itemId, startAt, endAt)).thenReturn(empty);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        doNothing().when(rentalLogService).save(any(RentalLog.class));
        assertThat(reservationService.createReservation(itemId, customer.getId(), startAt, endAt).equals(responseDto)).isTrue();
    }

    @Test
    void testCreateReservation_Conflict() {
        when(reservationRepository.findConflictingReservations(itemId, startAt, endAt)).thenReturn(Collections.singletonList(reservation));
        ReservationConflictException exception = assertThrows(ReservationConflictException.class, () -> reservationService.createReservation(itemId, userId, startAt, endAt));
        assertEquals("해당 물건은 이미 그 시간에 예약이 있습니다.", exception.getMessage());
    }

    @Test
    void testCreateReservation_ItemNotFound() {
        when(reservationRepository.findConflictingReservations(itemId, startAt, endAt)).thenReturn(Collections.emptyList());
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(itemId, userId, startAt, endAt));
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void testCreateReservation_UserNotFound() {
        when(reservationRepository.findConflictingReservations(itemId, startAt, endAt)).thenReturn(Collections.emptyList());
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(itemId, userId, startAt, endAt));
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }


    @Test
    void getReservations() {
        ReservationResponseDto responseDto = new ReservationResponseDto(reservationId, customer.getNickname(), item.getName(), startAt, endAt);
        List<ReservationResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(responseDto);
        when(reservationRepository.findAllReservations()).thenReturn(Collections.singletonList(reservation));
        assertThat(reservationService.getReservations()).isEqualTo(responseDtoList);
    }


    @Test
    void searchAndConvertReservations(){
        when(reservationService.searchReservations(customer.getId(), itemId)).thenReturn(Collections.singletonList(reservation));
        List<ReservationResponseDto> responseDtoList = new ArrayList<>();
        responseDtoList.add(new ReservationResponseDto(reservationId, customer.getNickname(), item.getName(), startAt, endAt));


        assertThat(reservationService.searchAndConvertReservations(customer.getId(), itemId)).isEqualTo(responseDtoList);
    }

    @Test
    void searchReservations() {
        when(reservationRepository.findByUserIdAndItemId(userId, itemId)).thenReturn(Collections.singletonList(reservation));
        assertThat(reservationService.searchReservations(userId, itemId)).isEqualTo(Collections.singletonList(reservation));
    }

    @Test
    void updateReservationStatus() {
        ReservationStatus status = ReservationStatus.APPROVED;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        assertThat(reservationService.updateReservationStatus(reservationId, status)).isEqualTo(new UpdateReservationResponseDto(reservationId, status));
    }

    @Test
    void testUpdateReservationStatus_ReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(1L, ReservationStatus.APPROVED));
        assertEquals("해당 ID에 맞는 데이터가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void testUpdateReservationStatus_InvalidStatusChangeToApproved() {
        reservation.setStatus(ReservationStatus.CANCELED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(1L, ReservationStatus.APPROVED));
        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", exception.getMessage());
    }

    @Test
    void testUpdateReservationStatus_InvalidStatusChangeToCanceled() {
        reservation.setStatus(ReservationStatus.EXPIRED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(1L, ReservationStatus.CANCELED));
        assertEquals("EXPIRED 상태인 예약은 취소할 수 없습니다.", exception.getMessage());
    }

    @Test
    void testUpdateReservationStatus_InvalidStatusChangeToExpired() {
        reservation.setStatus(ReservationStatus.APPROVED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reservationService.updateReservationStatus(1L, ReservationStatus.EXPIRED));
        assertEquals("PENDING 상태만 APPROVED로 변경 가능합니다.", exception.getMessage());
    }

    @Test
    void testUpdateReservationStatus_InvalidStatus() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        assertThrows(NullPointerException.class, () -> reservationService.updateReservationStatus(1L, null));

    }
}