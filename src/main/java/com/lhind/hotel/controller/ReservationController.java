package com.lhind.hotel.controller;

import com.lhind.hotel.dto.ReservationRequestDto;
import com.lhind.hotel.dto.ReservationResponseDto;
import com.lhind.hotel.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(
        name = "Reservations",
        description = "Endpoints for managing hotel reservations"
)
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "Create a reservation",
            description = """
                    Creates a reservation.
                    Guest ID and room ID must be supplied in the request body.
                    """
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ReservationResponseDto> createReservation(
            @Valid @RequestBody
            ReservationRequestDto reservationDto
    ) {

        ReservationResponseDto createdReservation =
                reservationService.createReservation(
                        reservationDto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReservation);
    }

    @Operation(
            summary = "Get all reservations",
            description = "Returns all reservations."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<ReservationResponseDto>>
    getAllReservations() {

        return ResponseEntity.ok(
                reservationService.getAllReservations()
        );
    }

    @Operation(
            summary = "Get reservation by ID",
            description = "Returns a reservation using its ID."
    )
    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ReservationResponseDto>
    getReservationById(
            @Parameter(
                    description = "Unique ID of the reservation",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId
    ) {

        ReservationResponseDto reservation =
                reservationService
                        .getReservationById(reservationId);

        return ResponseEntity.ok(reservation);
    }

    @Operation(
            summary = "Get reservations by guest",
            description = "Returns all reservations belonging to a guest."
    )
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<ReservationResponseDto>>
    getByGuestId(
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
            description = "Returns all reservations associated with a room."
    )
    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<ReservationResponseDto>>
    getByRoomId(
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
            description = "Updates reservation dates, guest, room and number of guests."
    )
    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ReservationResponseDto>
    updateReservation(
            @Parameter(
                    description = "ID of the reservation to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId,

            @Valid @RequestBody
            ReservationRequestDto reservationDto
    ) {

        ReservationResponseDto updatedReservation =
                reservationService.updateReservation(
                        reservationId,
                        reservationDto
                );

        return ResponseEntity.ok(updatedReservation);
    }

    @Operation(
            summary = "Cancel a reservation",
            description = "Changes the reservation status to CANCELLED."
    )
    @PatchMapping("/{reservationId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponseDto>
    cancelReservation(
            @Parameter(
                    description = "ID of the reservation to cancel",
                    example = "1",
                    required = true
            )
            @PathVariable Long reservationId
    ) {

        ReservationResponseDto cancelledReservation =
                reservationService
                        .cancelReservation(reservationId);

        return ResponseEntity.ok(cancelledReservation);
    }

    @Operation(
            summary = "Delete a reservation",
            description = "Deletes an existing reservation."
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

        reservationService.deleteReservation(
                reservationId
        );

        return ResponseEntity.noContent().build();
    }
}