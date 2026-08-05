package com.lhind.hotel.service;

import com.lhind.hotel.dto.ReservationRequestDto;
import com.lhind.hotel.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {

    ReservationResponseDto createReservation(
            ReservationRequestDto reservationDto
    );

    List<ReservationResponseDto> getAllReservations();

    ReservationResponseDto getReservationById(
            Long reservationId
    );

    List<ReservationResponseDto> getReservationsByGuestId(
            Long guestId
    );

    List<ReservationResponseDto> getReservationsByRoomId(
            Long roomId
    );

    ReservationResponseDto updateReservation(
            Long reservationId,
            ReservationRequestDto reservationDto
    );

    ReservationResponseDto cancelReservation(
            Long reservationId
    );

    void deleteReservation(Long reservationId);
}