package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.service.GuestService;
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
@RequestMapping("/api/guests")
@Tag(
        name = "Guests",
        description = "Guest Management — demonstrates Log4j2 levels TRACE → FATAL"
)
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @Operation(
            summary = "Create a guest",
            description = """
                    Creates a new guest.
                    Logs TRACE, DEBUG and INFO.
                    Logs ERROR when another guest has the same email.
                    """
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<Guest> createGuest(
            @Valid @RequestBody Guest guest
    ) {
        Guest createdGuest =
                guestService.createGuest(guest);

        if (createdGuest == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdGuest);
    }

    @Operation(
            summary = "Get all guests",
            description = """
                    Returns all guests.
                    Logs TRACE on entry, DEBUG while reading the database
                    and INFO with the number of returned guests.
                    """
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(
                guestService.getAllGuests()
        );
    }

    @Operation(
            summary = "Get guest by ID",
            description = """
                    Searches for a guest by ID.
                    Logs INFO when found and WARN when not found.
                    """
    )
    @GetMapping("/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Guest> getGuestById(
            @Parameter(
                    description = "Unique ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {
        Optional<Guest> guest =
                guestService.getGuestById(guestId);

        return guest
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Search guest by email",
            description = """
                    Searches for a guest by email.
                    Logs DEBUG during lookup, INFO when found
                    and WARN when the guest does not exist.
                    """
    )
    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Guest> getGuestByEmail(
            @Parameter(
                    description = "Email of the guest",
                    example = "guest@hotel.com",
                    required = true
            )
            @RequestParam String email
    ) {
        Optional<Guest> guest =
                guestService.getGuestByEmail(email);

        return guest
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Update a guest",
            description = """
                    Updates an existing guest.
                    Logs TRACE and DEBUG during validation,
                    INFO on success, WARN when not found
                    and ERROR when the email is already used.
                    """
    )
    @PutMapping("/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<Guest> updateGuest(
            @Parameter(
                    description = "ID of the guest to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId,

            @Valid @RequestBody Guest guest
    ) {
        if (guestService.getGuestById(guestId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Guest updatedGuest =
                guestService.updateGuest(guestId, guest);

        if (updatedGuest == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        return ResponseEntity.ok(updatedGuest);
    }

    @Operation(
            summary = "Delete a guest",
            description = """
                    Deletes an existing guest.
                    Logs TRACE and DEBUG during deletion,
                    INFO on success and ERROR when not found.
                    """
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
        boolean deleted =
                guestService.deleteGuest(guestId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}