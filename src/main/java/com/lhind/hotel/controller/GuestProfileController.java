package com.lhind.hotel.controller;

import com.lhind.hotel.entity.GuestProfile;
import com.lhind.hotel.service.GuestProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/guest-profiles")
public class GuestProfileController {

    private final GuestProfileService guestProfileService;

    public GuestProfileController(
            GuestProfileService guestProfileService
    ) {
        this.guestProfileService = guestProfileService;
    }

    // Krijon profil për një guest ekzistues

    @PostMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('isAuthenticated()')")

    public ResponseEntity<GuestProfile> createGuestProfile(
            @PathVariable Long guestId,
            @Valid @RequestBody GuestProfile guestProfile
    ) {
        GuestProfile createdProfile =
                guestProfileService.createGuestProfile(
                        guestId,
                        guestProfile
                );

        if (createdProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProfile);
    }

    // Kthen të gjitha profilet
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<GuestProfile>> getAllGuestProfiles() {

        List<GuestProfile> profiles =
                guestProfileService.getAllGuestProfiles();

        return ResponseEntity.ok(profiles);
    }

    // Kthen profilin sipas ID-së së profilit
    @GetMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<GuestProfile> getGuestProfileById(
            @PathVariable Long profileId
    ) {
        Optional<GuestProfile> optionalProfile =
                guestProfileService.getGuestProfileById(profileId);

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalProfile.get());
    }

    // Kthen profilin sipas ID-së së guest-it
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<GuestProfile> getProfileByGuestId(
            @PathVariable Long guestId
    ) {
        Optional<GuestProfile> optionalProfile =
                guestProfileService.getGuestProfileByGuestId(guestId);

        if (optionalProfile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalProfile.get());
    }

    // Përditëson profilin
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")

    @PutMapping("/{profileId}")
    public ResponseEntity<GuestProfile> updateGuestProfile(
            @PathVariable Long profileId,
            @Valid @RequestBody GuestProfile guestProfile
    ) {
        GuestProfile updatedProfile =
                guestProfileService.updateGuestProfile(
                        profileId,
                        guestProfile
                );

        if (updatedProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProfile);
    }

    // Fshin profilin
    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN')")

    public ResponseEntity<Void> deleteGuestProfile(
            @PathVariable Long profileId
    ) {
        boolean deleted =
                guestProfileService.deleteGuestProfile(profileId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}