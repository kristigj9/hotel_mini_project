package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.entity.ReservationStatus;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.repo.ReservationRepo;
import com.lhind.hotel.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl
        implements ReservationService {

    private static final Logger log =
            LogManager.getLogger(
                    ReservationServiceImpl.class
            );

    private final ReservationRepo reservationRepo;
    private final GuestRepo guestRepo;
    private final RoomRepo roomRepo;

    @Override
    public Reservation createReservation(
            Long guestId,
            Long roomId,
            Reservation reservation
    ) {
        log.trace(
                "Entering createReservation() with guestId={}, roomId={}",
                guestId,
                roomId
        );

        log.debug(
                "Validating guest and room existence"
        );

        Guest guest =
                guestRepo.findById(guestId)
                        .orElseThrow(() -> {
                            log.error(
                                    "Reservation creation failed because guestId={} does not exist",
                                    guestId
                            );

                            return new IllegalArgumentException(
                                    "Guest with ID "
                                            + guestId
                                            + " does not exist"
                            );
                        });

        Room room =
                roomRepo.findById(roomId)
                        .orElseThrow(() -> {
                            log.error(
                                    "Reservation creation failed because roomId={} does not exist",
                                    roomId
                            );

                            return new IllegalArgumentException(
                                    "Room with ID "
                                            + roomId
                                            + " does not exist"
                            );
                        });

        if (reservation.getCheckInDate() == null
                || reservation.getCheckOutDate() == null) {

            log.error(
                    "Reservation creation failed because dates are missing"
            );

            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!reservation.getCheckOutDate()
                .isAfter(reservation.getCheckInDate())) {

            log.error(
                    "Reservation creation failed because check-out is not after check-in"
            );

            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }

        log.debug(
                "Checking overlapping reservations for roomId={}",
                roomId
        );

        long overlaps =
                reservationRepo.countOverlappingReservations(
                        roomId,
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate()
                );

        if (overlaps > 0) {
            log.warn(
                    "RoomId={} is already reserved between {} and {}",
                    roomId,
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate()
            );

            throw new IllegalArgumentException(
                    "Room is already reserved for the selected dates"
            );
        }

        reservation.setGuest(guest);
        reservation.setRoom(room);

        if (reservation.getStatus() == null) {
            reservation.setStatus(
                    ReservationStatus.CONFIRMED
            );
        }

        Reservation savedReservation =
                reservationRepo.save(reservation);

        log.info(
                "Reservation created successfully. id={}, guestId={}, roomId={}",
                savedReservation.getId(),
                guestId,
                roomId
        );

        return savedReservation;
    }

    @Override
    public List<Reservation> getAllReservations() {

        log.trace("Entering getAllReservations()");
        log.debug("Retrieving all reservations");

        List<Reservation> reservations =
                reservationRepo.findAll();

        log.info(
                "Retrieved {} reservations",
                reservations.size()
        );

        return reservations;
    }

    @Override
    public Optional<Reservation> getReservationById(
            Long reservationId
    ) {
        log.trace(
                "Entering getReservationById() with id={}",
                reservationId
        );

        Optional<Reservation> reservation =
                reservationRepo.findById(reservationId);

        if (reservation.isEmpty()) {
            log.warn(
                    "Reservation with id={} was not found",
                    reservationId
            );
        } else {
            log.info(
                    "Reservation with id={} found successfully",
                    reservationId
            );
        }

        return reservation;
    }

    @Override
    public List<Reservation> getReservationsByGuestId(
            Long guestId
    ) {
        log.trace(
                "Entering getReservationsByGuestId() with guestId={}",
                guestId
        );

        List<Reservation> reservations =
                reservationRepo.findByGuestId(guestId);

        if (reservations.isEmpty()) {
            log.warn(
                    "No reservations found for guestId={}",
                    guestId
            );
        } else {
            log.info(
                    "Retrieved {} reservations for guestId={}",
                    reservations.size(),
                    guestId
            );
        }

        return reservations;
    }

    @Override
    public List<Reservation> getReservationsByRoomId(
            Long roomId
    ) {
        log.trace(
                "Entering getReservationsByRoomId() with roomId={}",
                roomId
        );

        List<Reservation> reservations =
                reservationRepo.findByRoomId(roomId);

        if (reservations.isEmpty()) {
            log.warn(
                    "No reservations found for roomId={}",
                    roomId
            );
        } else {
            log.info(
                    "Retrieved {} reservations for roomId={}",
                    reservations.size(),
                    roomId
            );
        }

        return reservations;
    }

    @Override
    public Reservation updateReservation(
            Long reservationId,
            Reservation reservation
    ) {
        log.trace(
                "Entering updateReservation() with id={}",
                reservationId
        );

        Optional<Reservation> optionalReservation =
                reservationRepo.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            log.warn(
                    "Reservation update failed because id={} was not found",
                    reservationId
            );

            return null;
        }

        if (reservation.getCheckInDate() == null
                || reservation.getCheckOutDate() == null) {

            log.error(
                    "Reservation update failed because dates are missing. id={}",
                    reservationId
            );

            return null;
        }

        if (!reservation.getCheckOutDate()
                .isAfter(reservation.getCheckInDate())) {

            log.error(
                    "Reservation update failed because dates are invalid. id={}",
                    reservationId
            );

            return null;
        }

        Reservation existingReservation =
                optionalReservation.get();

        log.debug(
                "Checking overlaps for reservationId={}",
                reservationId
        );

        long overlaps =
                reservationRepo
                        .countOverlappingReservationsExceptCurrent(
                                existingReservation
                                        .getRoom()
                                        .getId(),
                                reservation.getCheckInDate(),
                                reservation.getCheckOutDate(),
                                reservationId
                        );

        if (overlaps > 0) {
            log.warn(
                    "Reservation update rejected because dates overlap. id={}",
                    reservationId
            );

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
            existingReservation.setStatus(
                    reservation.getStatus()
            );
        }

        Reservation updatedReservation =
                reservationRepo.save(
                        existingReservation
                );

        log.info(
                "Reservation updated successfully. id={}",
                reservationId
        );

        return updatedReservation;
    }

    @Override
    public Reservation cancelReservation(
            Long reservationId
    ) {
        log.trace(
                "Entering cancelReservation() with id={}",
                reservationId
        );

        Optional<Reservation> optionalReservation =
                reservationRepo.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            log.warn(
                    "Reservation cancellation failed because id={} was not found",
                    reservationId
            );

            return null;
        }

        Reservation reservation =
                optionalReservation.get();

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );

        Reservation cancelledReservation =
                reservationRepo.save(reservation);

        log.info(
                "Reservation cancelled successfully. id={}",
                reservationId
        );

        return cancelledReservation;
    }

    @Override
    public boolean deleteReservation(
            Long reservationId
    ) {
        log.trace(
                "Entering deleteReservation() with id={}",
                reservationId
        );

        if (!reservationRepo.existsById(
                reservationId
        )) {
            log.error(
                    "Reservation deletion failed because id={} does not exist",
                    reservationId
            );

            return false;
        }

        reservationRepo.deleteById(reservationId);

        log.info(
                "Reservation deleted successfully. id={}",
                reservationId
        );

        return true;
    }
}