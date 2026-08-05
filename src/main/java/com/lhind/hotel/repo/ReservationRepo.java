package com.lhind.hotel.repo;

import com.lhind.hotel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuest_Id(Long guestId);

    List<Reservation> findByRoom_Id(Long roomId);

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
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
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
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("reservationId") Long reservationId
    );
}