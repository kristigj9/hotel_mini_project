package com.lhind.hotel.controller;

import com.lhind.hotel.dto.GuestDto;
import com.lhind.hotel.service.GuestService;
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
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Tag(
        name = "Guests",
        description = "Endpoints for managing hotel guests"
)
public class GuestController {

    private final GuestService guestService;

    @Operation(
            summary = "Create a guest",
            description = "Creates a new guest."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestDto> createGuest(
            @Valid @RequestBody GuestDto guestDto
    ) {

        GuestDto createdGuest =
                guestService.createGuest(guestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdGuest);
    }

    @Operation(
            summary = "Get all guests",
            description = "Returns all registered guests."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<GuestDto>> getAllGuests() {

        return ResponseEntity.ok(
                guestService.getAllGuests()
        );
    }

    @Operation(
            summary = "Get guest by ID",
            description = "Returns a guest using the guest ID."
    )
    @GetMapping("/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<GuestDto> getGuestById(
            @Parameter(
                    description = "Unique ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {

        GuestDto guest =
                guestService.getGuestById(guestId);

        return ResponseEntity.ok(guest);
    }

    @Operation(
            summary = "Search guest by email",
            description = "Returns a guest using the email address."
    )
    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<GuestDto> getGuestByEmail(
            @Parameter(
                    description = "Email of the guest",
                    example = "guest@hotel.com",
                    required = true
            )
            @RequestParam String email
    ) {

        GuestDto guest =
                guestService.getGuestByEmail(email);

        return ResponseEntity.ok(guest);
    }

    @Operation(
            summary = "Update a guest",
            description = "Updates the information of an existing guest."
    )
    @PutMapping("/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestDto> updateGuest(
            @Parameter(
                    description = "ID of the guest to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId,

            @Valid @RequestBody GuestDto guestDto
    ) {

        GuestDto updatedGuest =
                guestService.updateGuest(
                        guestId,
                        guestDto
                );

        return ResponseEntity.ok(updatedGuest);
    }

    @Operation(
            summary = "Delete a guest",
            description = "Deletes an existing guest."
    )
    @DeleteMapping("/{guestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGuest(
            @Parameter(
                    description = "ID of the guest to delete",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {

        guestService.deleteGuest(guestId);

        return ResponseEntity.noContent().build();
    }
}