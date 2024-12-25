package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationStatus;
import com.example.demo.entity.User;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    private User customer;
    private User owner;
    private User manager;
    private Item item;
    private Reservation reservation;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        customer = new User("user", "user1@test", "nickname", PasswordEncoder.encode("password"));
        manager = new User("user", "user2@test", "nickname", PasswordEncoder.encode("password"));
        owner = new User("user", "user3@test", "nickname", PasswordEncoder.encode("password"));
        userRepository.save(customer);
        userRepository.save(manager);
        userRepository.save(owner);
        item = new Item("item1","desc",manager ,owner);
        itemRepository.save(item);
        reservation = new Reservation(item,customer,LocalDateTime.now(),LocalDateTime.now().plusMinutes(30));
        reservation.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservation);
    }



    @Test
    @Transactional
    @Rollback()
    void testFindByUserId() {
        List<Reservation> reservations = reservationRepository.findByUserId(customer.getId());
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    @Transactional
    @Rollback()
    void testFindByItemId() {
        List<Reservation> reservations = reservationRepository.findByItemId(item.getId());
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    @Transactional
    @Rollback()
    void testFindConflictingReservations() {
        LocalDateTime startAt = reservation.getStartAt().minusHours(1);
        LocalDateTime endAt = reservation.getEndAt().plusHours(1);
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(item.getId(), startAt, endAt);
        assertNotNull(conflictingReservations);
        assertFalse(conflictingReservations.isEmpty());
        assertEquals(1, conflictingReservations.size());
        assertEquals(reservation, conflictingReservations.get(0));
    }

    @Test
    @Transactional
    @Rollback()
    void testFindAllReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }

    @Test
    @Transactional()
    @Rollback()
    void testUser() {
        List<Reservation> reservations = reservationRepository.user(customer);
        assertNotNull(reservations);
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
        assertEquals(reservation, reservations.get(0));
    }
}