package com.lhind.hotel.controller;

import com.lhind.hotel.dto.HotelDto;
import com.lhind.hotel.service.HotelService;
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
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(
        name = "Hotels",
        description = "Endpoints for managing hotels"
)
public class HotelController {

    private final HotelService hotelService;

    @Operation(
            summary = "Create a hotel",
            description = "Creates a new hotel."
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelDto> createHotel(
            @Valid @RequestBody HotelDto hotelDto
    ) {

        HotelDto createdHotel =
                hotelService.createHotel(hotelDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    @Operation(
            summary = "Get all hotels",
            description = "Returns all hotels."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HotelDto>> getAllHotels() {

        return ResponseEntity.ok(
                hotelService.getAllHotels()
        );
    }

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns a hotel using its ID."
    )
    @GetMapping("/{hotelId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<HotelDto> getHotelById(
            @Parameter(
                    description = "Unique ID of the hotel",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId
    ) {

        HotelDto hotel =
                hotelService.getHotelById(hotelId);

        return ResponseEntity.ok(hotel);
    }

    @Operation(
            summary = "Search hotels by city",
            description = "Returns hotels whose city matches the search value."
    )
    @GetMapping("/city")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HotelDto>> getHotelsByCity(
            @Parameter(
                    description = "City where hotels will be searched",
                    example = "Tirana",
                    required = true
            )
            @RequestParam String city
    ) {

        return ResponseEntity.ok(
                hotelService.getHotelsByCity(city)
        );
    }

    @Operation(
            summary = "Search hotels by name",
            description = "Returns hotels matching a full or partial name."
    )
    @GetMapping("/name")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HotelDto>> getHotelsByName(
            @Parameter(
                    description = "Full or partial hotel name",
                    example = "Plaza",
                    required = true
            )
            @RequestParam String name
    ) {

        return ResponseEntity.ok(
                hotelService.getHotelsByName(name)
        );
    }

    @Operation(
            summary = "Update a hotel",
            description = "Updates an existing hotel."
    )
    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelDto> updateHotel(
            @Parameter(
                    description = "ID of the hotel to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId,

            @Valid @RequestBody HotelDto hotelDto
    ) {

        HotelDto updatedHotel =
                hotelService.updateHotel(
                        hotelId,
                        hotelDto
                );

        return ResponseEntity.ok(updatedHotel);
    }

    @Operation(
            summary = "Delete a hotel",
            description = """
                    Deletes an existing hotel.
                    The dbAvailable parameter can be used to simulate
                    database unavailability for the logging exercise.
                    """
    )
    @DeleteMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHotel(
            @Parameter(
                    description = "ID of the hotel to delete",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId,

            @Parameter(
                    description = "Set false to simulate database unavailability",
                    example = "true"
            )
            @RequestParam(defaultValue = "true")
            boolean dbAvailable
    ) {

        hotelService.deleteHotel(
                hotelId,
                dbAvailable
        );

        return ResponseEntity.noContent().build();
    }
}