package com.lhind.hotel.controller;

import com.lhind.hotel.dto.GuestProfileDto;
import com.lhind.hotel.service.GuestProfileService;
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
@RequestMapping("/api/guest-profiles")
@RequiredArgsConstructor
@Tag(
        name = "Guest Profiles",
        description = "Endpoints for managing guest profiles"
)
public class GuestProfileController {

    private final GuestProfileService guestProfileService;

    @Operation(
            summary = "Create a guest profile",
            description = """
                    Creates a profile for an existing guest.
                    The guest ID must be supplied in the request body.
                    """
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfileDto> createGuestProfile(
            @Valid @RequestBody GuestProfileDto guestProfileDto
    ) {

        GuestProfileDto createdProfile =
                guestProfileService.createGuestProfile(
                        guestProfileDto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProfile);
    }

    @Operation(
            summary = "Get all guest profiles",
            description = "Returns all guest profiles."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<GuestProfileDto>>
    getAllGuestProfiles() {

        return ResponseEntity.ok(
                guestProfileService.getAllGuestProfiles()
        );
    }

    @Operation(
            summary = "Get guest profile by profile ID",
            description = "Returns a guest profile using its profile ID."
    )
    @GetMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfileDto> getGuestProfileById(
            @Parameter(
                    description = "Unique ID of the guest profile",
                    example = "1",
                    required = true
            )
            @PathVariable Long profileId
    ) {

        GuestProfileDto profile =
                guestProfileService.getGuestProfileById(
                        profileId
                );

        return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Get guest profile by guest ID",
            description = "Returns the profile associated with a guest."
    )
    @GetMapping("/guest/{guestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfileDto> getProfileByGuestId(
            @Parameter(
                    description = "Unique ID of the guest",
                    example = "1",
                    required = true
            )
            @PathVariable Long guestId
    ) {

        GuestProfileDto profile =
                guestProfileService
                        .getGuestProfileByGuestId(guestId);

        return ResponseEntity.ok(profile);
    }

    @Operation(
            summary = "Update a guest profile",
            description = "Updates an existing guest profile."
    )
    @PutMapping("/{profileId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'GUEST')")
    public ResponseEntity<GuestProfileDto> updateGuestProfile(
            @Parameter(
                    description = "ID of the profile to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long profileId,

            @Valid @RequestBody
            GuestProfileDto guestProfileDto
    ) {

        GuestProfileDto updatedProfile =
                guestProfileService.updateGuestProfile(
                        profileId,
                        guestProfileDto
                );

        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(
            summary = "Delete a guest profile",
            description = "Deletes an existing guest profile."
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

        guestProfileService.deleteGuestProfile(profileId);

        return ResponseEntity.noContent().build();
    }
}