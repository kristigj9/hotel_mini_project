package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.entity.ReservationStatus;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.repo.ReservationRepo;
import com.lhind.hotel.repo.RoomRepo;
import com.lhind.hotel.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepo reservationRepo;
    private final GuestRepo guestRepo;
    private final RoomRepo roomRepo;

    public ReservationServiceImpl(
            ReservationRepo reservationRepo,
            GuestRepo guestRepo,
            RoomRepo roomRepo
    ) {
        this.reservationRepo = reservationRepo;
        this.guestRepo = guestRepo;
        this.roomRepo = roomRepo;
    }

    @Override
    public Reservation createReservation(
            Long guestId,
            Long roomId,
            Reservation reservation
    ) {
        Guest guest = guestRepo.findById(guestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Guest with ID " + guestId + " does not exist"
                        )
                );

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Room with ID " + roomId + " does not exist"
                        )
                );

        if (reservation.getCheckInDate() == null
                || reservation.getCheckOutDate() == null) {
            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!reservation.getCheckOutDate()
                .isAfter(reservation.getCheckInDate())) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        long overlappingReservations =
                reservationRepo.countOverlappingReservations(
                        roomId,
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate()
                );

        if (overlappingReservations > 0) {
            throw new IllegalArgumentException(
                    "Room is already reserved for the selected dates"
            );
        }

        reservation.setGuest(guest);
        reservation.setRoom(room);

        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }

        return reservationRepo.save(reservation);
    }


    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepo.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(Long reservationId) {
        return reservationRepo.findById(reservationId);
    }

    @Override
    public List<Reservation> getReservationsByGuestId(Long guestId) {
        return reservationRepo.findByGuestId(guestId);
    }

    @Override
    public List<Reservation> getReservationsByRoomId(Long roomId) {
        return reservationRepo.findByRoomId(roomId);
    }

    @Override
    public Reservation updateReservation(
            Long reservationId,
            Reservation reservation
    ) {
        Optional<Reservation> optionalReservation =
                reservationRepo.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            return null;
        }

        if (reservation.getCheckInDate() == null
                || reservation.getCheckOutDate() == null) {
            return null;
        }

        if (!reservation.getCheckOutDate()
                .isAfter(reservation.getCheckInDate())) {
            return null;
        }

        Reservation existingReservation = optionalReservation.get();

        long overlappingReservations =
                reservationRepo.countOverlappingReservationsExceptCurrent(
                        existingReservation.getRoom().getId(),
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate(),
                        reservationId
                );

        if (overlappingReservations > 0) {
            return null;
        }

        existingReservation.setCheckInDate(
                reservation.getCheckInDate()
        );

        existingReservation.setCheckOutDate(
                reservation.getCheckOutDate()
        );

        existingReservation.setNumberOfGuests(
                reservation.getNumberOfGuests()
        );

        existingReservation.setTotalPrice(
                reservation.getTotalPrice()
        );

        if (reservation.getStatus() != null) {
            existingReservation.setStatus(reservation.getStatus());
        }

        return reservationRepo.save(existingReservation);
    }

    @Override
    public Reservation cancelReservation(Long reservationId) {

        Optional<Reservation> optionalReservation =
                reservationRepo.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            return null;
        }

        Reservation reservation = optionalReservation.get();
        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationRepo.save(reservation);
    }

    @Override
    public boolean deleteReservation(Long reservationId) {

        if (!reservationRepo.existsById(reservationId)) {
            return false;
        }

        reservationRepo.deleteById(reservationId);
        return true;
    }
}