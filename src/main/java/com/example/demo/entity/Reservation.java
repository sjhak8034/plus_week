package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.PENDING; // PENDING, APPROVED, CANCELED, EXPIRED

    public Reservation(Item item, User user, LocalDateTime startAt, LocalDateTime endAt) {
        this.item = item;
        this.user = user;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reservation() {}

    @Transient
    public void setId(Long id) {
        this.id = id;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    @Transient
    public void setStatus(ReservationStatus status){
        this.status = status;
    }

}
