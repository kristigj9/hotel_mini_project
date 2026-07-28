package com.lhind.hotel.service;

import com.lhind.hotel.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Reservation createReservation(
            Long guestId,
            Long roomId,
            Reservation reservation
    );

    List<Reservation> getAllReservations();

    Optional<Reservation> getReservationById(Long reservationId);

    List<Reservation> getReservationsByGuestId(Long guestId);

    List<Reservation> getReservationsByRoomId(Long roomId);

    Reservation updateReservation(
            Long reservationId,
            Reservation reservation
    );

    Reservation cancelReservation(Long reservationId);

    boolean deleteReservation(Long reservationId);
}