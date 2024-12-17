package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.UpdateReservationResponseDto;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservationRequestDto.getItemId(),
                reservationRequestDto.getUserId(),
                reservationRequestDto.getStartAt(),
                reservationRequestDto.getEndAt()));
    }

    @PatchMapping("/{id}/update-status")
    public ResponseEntity<UpdateReservationResponseDto> updateReservation(@PathVariable Long id, @RequestBody ReservationStatus status) {
        return new ResponseEntity<>(reservationService.updateReservationStatus(id, status), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> findAll() {
        return new ResponseEntity<>(reservationService.getReservations(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ReservationResponseDto>> searchAll(@RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Long itemId) {
        return new ResponseEntity<>(reservationService.searchAndConvertReservations(userId, itemId), HttpStatus.OK);
    }
}
