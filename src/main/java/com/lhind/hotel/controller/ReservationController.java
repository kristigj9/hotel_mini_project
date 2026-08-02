package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@Tag(
        name = "Reservations",
        description = "Reservation Management — demonstrates Log4j2 levels TRACE → FATAL"
)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService
    ) {
        this.reservationService = reservationService;
    }

    @Operation(
            summary = "Create a reservation",
            description = """
                    Creates a reservation for an existing guest and room.
                    Logs INFO on success, WARN for overlapping reservations
                    and ERROR for invalid dates or missing entities.
                    """
    )
    @PostMapping("/guest/{guestId}/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Reservation> createReservation(
            @Parameter(
                    description = "ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId,

            @Parameter(
                    description = "ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId,

            @Valid @RequestBody Reservation reservation
    ) {
        Reservation createdReservation =
                reservationService.createReservation(
                        guestId,
                        roomId,
                        reservation
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReservation);
    }

    @Operation(
            summary = "Get all reservations",
            description = "Returns all reservations and logs the result count"
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(
                reservationService.getAllReservations()
        );
    }

    @Operation(
            summary = "Get reservation by ID",
            description = "Logs INFO when found and WARN when not found"
    )
    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Reservation> getReservationById(
            @Parameter(
                    description = "Unique ID of the reservation",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId
    ) {
        Optional<Reservation> reservation =
                reservationService
                        .getReservationById(reservationId);

        return reservation
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Get reservations by guest",
            description = "Returns all reservations belonging to a guest"
    )
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Reservation>> getByGuestId(
            @Parameter(
                    description = "ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {
        return ResponseEntity.ok(
                reservationService
                        .getReservationsByGuestId(guestId)
        );
    }

    @Operation(
            summary = "Get reservations by room",
            description = "Returns all reservations associated with a room"
    )
    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Reservation>> getByRoomId(
            @Parameter(
                    description = "ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId
    ) {
        return ResponseEntity.ok(
                reservationService
                        .getReservationsByRoomId(roomId)
        );
    }

    @Operation(
            summary = "Update a reservation",
            description = """
                    Updates reservation dates and information.
                    Logs WARN for overlapping dates and ERROR for invalid data.
                    """
    )
    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Reservation> updateReservation(
            @Parameter(
                    description = "ID of the reservation to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId,

            @Valid @RequestBody Reservation reservation
    ) {
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

    @Operation(
            summary = "Cancel a reservation",
            description = """
                    Changes reservation status to CANCELLED.
                    Logs INFO on success and WARN when not found.
                    """
    )
    @PatchMapping("/{reservationId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Reservation> cancelReservation(
            @Parameter(
                    description = "ID of the reservation to cancel",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId
    ) {
        Reservation cancelledReservation =
                reservationService
                        .cancelReservation(reservationId);

        if (cancelledReservation == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cancelledReservation);
    }

    @Operation(
            summary = "Delete a reservation",
            description = "Logs INFO on success and ERROR when not found"
    )
    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(
                    description = "ID of the reservation to delete",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId
    ) {
        boolean deleted =
                reservationService
                        .deleteReservation(reservationId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}