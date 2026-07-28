package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService
    ) {
        this.reservationService = reservationService;
    }

    // Krijon një rezervim për një guest dhe një room
    @PostMapping("/guest/{guestId}/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<?> createReservation(
            @PathVariable Long guestId,
            @PathVariable Long roomId,
            @Valid @RequestBody Reservation reservation
    ) {
        try {
            Reservation createdReservation =
                    reservationService.createReservation(
                            guestId,
                            roomId,
                            reservation
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdReservation);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(exception.getMessage());
        }
    }

    // Kthen të gjitha rezervimet
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<Reservation>> getAllReservations() {

        List<Reservation> reservations =
                reservationService.getAllReservations();

        return ResponseEntity.ok(reservations);
    }

    // Kthen rezervimin sipas ID-së
    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long reservationId
    ) {
        Optional<Reservation> optionalReservation =
                reservationService.getReservationById(reservationId);

        if (optionalReservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalReservation.get());
    }

    // Kthen rezervimet e një guest-i
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<Reservation>> getByGuestId(
            @PathVariable Long guestId
    ) {
        List<Reservation> reservations =
                reservationService.getReservationsByGuestId(guestId);

        return ResponseEntity.ok(reservations);
    }

    // Kthen rezervimet e një dhome
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Reservation>> getByRoomId(
            @PathVariable Long roomId
    ) {
        List<Reservation> reservations =
                reservationService.getReservationsByRoomId(roomId);

        return ResponseEntity.ok(reservations);
    }

    // Përditëson një rezervim
    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody Reservation reservation
    ) {
        if (reservationService
                .getReservationById(reservationId)
                .isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reservation updatedReservation =
                reservationService.updateReservation(
                        reservationId,
                        reservation
                );

        if (updatedReservation == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return ResponseEntity.ok(updatedReservation);
    }

    // Anulon një rezervim
    @PatchMapping("/{reservationId}/cancel")
    @PreAuthorize("hasAnyRole('isAuthenticated()')")

    public ResponseEntity<Reservation> cancelReservation(
            @PathVariable Long reservationId
    ) {
        Reservation cancelledReservation =
                reservationService.cancelReservation(reservationId);

        if (cancelledReservation == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancelledReservation);
    }

    // Fshin një rezervim
    @PreAuthorize("hasAnyRole('ADMIN')")

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long reservationId
    ) {
        boolean deleted =
                reservationService.deleteReservation(reservationId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}