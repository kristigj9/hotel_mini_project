package com.lhind.hotel.controller;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.service.GuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // Krijon një guest të ri
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST','GUEST')")

    public ResponseEntity<Guest> createGuest(
            @Valid @RequestBody Guest guest
    ) {
        Guest createdGuest = guestService.createGuest(guest);

        if (createdGuest == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdGuest);
    }

    // Kthen të gjithë guests
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<Guest>> getAllGuests() {

        List<Guest> guests = guestService.getAllGuests();

        return ResponseEntity.ok(guests);
    }

    // Kthen guest sipas ID-së
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    @GetMapping("/{guestId}")
    public ResponseEntity<Guest> getGuestById(
            @PathVariable Long guestId
    ) {
        Optional<Guest> optionalGuest =
                guestService.getGuestById(guestId);

        if (optionalGuest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalGuest.get());
    }

    // Kërkon guest sipas email-it
    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<Guest> getGuestByEmail(
            @RequestParam String email
    ) {
        Optional<Guest> optionalGuest =
                guestService.getGuestByEmail(email);

        if (optionalGuest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalGuest.get());
    }

    // Përditëson një guest

    @PutMapping("/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST','GUEST')")

    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long guestId,
            @Valid @RequestBody Guest guest
    ) {
        if (guestService.getGuestById(guestId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Guest updatedGuest =
                guestService.updateGuest(guestId, guest);

        if (updatedGuest == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(updatedGuest);
    }

    // Fshin një guest
    @PreAuthorize("hasAnyRole('ADMIN')")

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Void> deleteGuest(
            @PathVariable Long guestId
    ) {
        boolean deleted = guestService.deleteGuest(guestId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
