package com.lhind.hotel.controller;

import com.lhind.hotel.entity.GuestProfile;
import com.lhind.hotel.service.GuestProfileService;
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
@RequestMapping("/api/guest-profiles")
@Tag(
        name = "Guest Profiles",
        description = "Guest Profile Management — demonstrates Log4j2 levels TRACE → FATAL"
)
public class GuestProfileController {

    private final GuestProfileService guestProfileService;

    public GuestProfileController(
            GuestProfileService guestProfileService
    ) {
        this.guestProfileService = guestProfileService;
    }

    @Operation(
            summary = "Create a guest profile",
            description = """
                    Creates a profile for an existing guest.
                    Logs TRACE, DEBUG and INFO.
                    Logs WARN when the guest does not exist.
                    """
    )
    @PostMapping("/guest/{guestId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GuestProfile> createGuestProfile(
            @Parameter(
                    description = "ID of the guest who owns the profile",
                    example = "1",
                    required = true
            )
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

    @Operation(
            summary = "Get all guest profiles",
            description = """
                    Returns all guest profiles.
                    Logs TRACE, DEBUG and INFO with the result count.
                    """
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<GuestProfile>> getAllGuestProfiles() {
        return ResponseEntity.ok(
                guestProfileService.getAllGuestProfiles()
        );
    }

    @Operation(
            summary = "Get guest profile by ID",
            description = """
                    Returns a profile by its ID.
                    Logs INFO when found and WARN when not found.
                    """
    )
    @GetMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<GuestProfile> getGuestProfileById(
            @Parameter(
                    description = "Unique ID of the guest profile",
                    example = "1",
                    required = true
            )
            @PathVariable Long profileId
    ) {
        Optional<GuestProfile> profile =
                guestProfileService.getGuestProfileById(profileId);

        return profile
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Get profile by guest ID",
            description = """
                    Returns the profile associated with a guest.
                    Logs INFO when found and WARN when no profile exists.
                    """
    )
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfile> getProfileByGuestId(
            @Parameter(
                    description = "ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {
        Optional<GuestProfile> profile =
                guestProfileService
                        .getGuestProfileByGuestId(guestId);

        return profile
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Update a guest profile",
            description = """
                    Updates an existing guest profile.
                    Logs TRACE and DEBUG during validation,
                    INFO on success and WARN when not found.
                    """
    )
    @PutMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfile> updateGuestProfile(
            @Parameter(
                    description = "ID of the profile to update",
                    example = "1",
                    required = true
            )
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

    @Operation(
            summary = "Delete a guest profile",
            description = """
                    Deletes a guest profile.
                    Logs INFO on success and ERROR when the profile does not exist.
                    """
    )
    @DeleteMapping("/{profileId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGuestProfile(
            @Parameter(
                    description = "ID of the profile to delete",
                    example = "1",
                    required = true
            )
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