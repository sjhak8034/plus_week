package com.example.demo.repository;

import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReservationQueryRepositoryImpl implements ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;
    private QReservation reservation = QReservation.reservation;
    private QUser user = QUser.user;
    private QItem item = QItem.item;
    @Override
    public List<Reservation> findByUserIdAndItemId(Long userId, Long itemId) {

        return queryFactory.selectFrom(reservation)
                .leftJoin(reservation.user).fetchJoin()
                .leftJoin(reservation.item).fetchJoin()
                .where(userIdEq(userId), itemIdEq(itemId))
                .fetch();
    }


    private BooleanExpression userIdEq(Long userId) {
        if (userId == null) {
            return null;
        }
        return reservation.user.id.eq(userId);
    }

    private BooleanExpression itemIdEq(Long itemId) {
        if (itemId == null) {
            return null;
        }
        return reservation.item.id.eq(itemId);
    }


}
