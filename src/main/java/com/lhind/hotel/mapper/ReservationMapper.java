package com.lhind.hotel.mapper;

import com.lhind.hotel.dto.ReservationRequestDto;
import com.lhind.hotel.dto.ReservationResponseDto;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation toEntity(
            ReservationRequestDto dto,
            Guest guest,
            Room room
    ) {
        if (dto == null) {
            return null;
        }

        Reservation reservation = new Reservation();

        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setGuest(guest);
        reservation.setRoom(room);

        return reservation;
    }

    public ReservationResponseDto toResponseDto(
            Reservation reservation
    ) {
        if (reservation == null) {
            return null;
        }

        ReservationResponseDto dto =
                new ReservationResponseDto();

        dto.setId(reservation.getId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setNumberOfGuests(reservation.getNumberOfGuests());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());

        if (reservation.getGuest() != null) {
            Guest guest = reservation.getGuest();

            dto.setGuestId(guest.getId());
            dto.setGuestFullName(
                    guest.getFirstName() + " " + guest.getLastName()
            );
        }

        if (reservation.getRoom() != null) {
            Room room = reservation.getRoom();

            dto.setRoomId(room.getId());
            dto.setRoomNumber(room.getRoomNumber());

            if (room.getHotel() != null) {
                dto.setHotelId(room.getHotel().getId());
                dto.setHotelName(room.getHotel().getName());
            }
        }

        return dto;
    }

    public void updateEntity(
            Reservation reservation,
            ReservationRequestDto dto,
            Guest guest,
            Room room
    ) {
        if (reservation == null || dto == null) {
            return;
        }

        reservation.setCheckInDate(dto.getCheckInDate());
        reservation.setCheckOutDate(dto.getCheckOutDate());
        reservation.setNumberOfGuests(dto.getNumberOfGuests());
        reservation.setGuest(guest);
        reservation.setRoom(room);
    }
}