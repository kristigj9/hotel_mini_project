package com.lhind.hotel.repo;

import com.lhind.hotel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepo
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuestId(Long guestId);

    List<Reservation> findByRoomId(Long roomId);

    @Query("""
        SELECT COUNT(r)
        FROM Reservation r
        WHERE r.room.id = :roomId
          AND r.status <> com.lhind.hotel.entity.ReservationStatus.CANCELLED
          AND r.checkInDate < :checkOutDate
          AND r.checkOutDate > :checkInDate
        """)
    long countOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate
    );

    @Query("""
        SELECT COUNT(r)
        FROM Reservation r
        WHERE r.room.id = :roomId
          AND r.id <> :reservationId
          AND r.status <> com.lhind.hotel.entity.ReservationStatus.CANCELLED
          AND r.checkInDate < :checkOutDate
          AND r.checkOutDate > :checkInDate
        """)
    long countOverlappingReservationsExceptCurrent(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDateTime checkInDate,
            @Param("checkOutDate") LocalDateTime checkOutDate,
            @Param("reservationId") Long reservationId
    );
}