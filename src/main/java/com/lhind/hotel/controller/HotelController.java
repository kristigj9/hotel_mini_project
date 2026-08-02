package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.service.HotelService;
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
@RequestMapping("/api/hotels")
@Tag(
        name = "Hotels",
        description = "Hotel Management — demonstrates Log4j2 levels TRACE → FATAL"
)
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Create a hotel",
            description = """
                    Creates a new hotel.
                    Logs TRACE, DEBUG and INFO.
                    Logs ERROR when hotel data is invalid.
                    """
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> createHotel(
            @Valid @RequestBody Hotel hotel
    ) {
        Hotel createdHotel =
                hotelService.createHotel(hotel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    @Operation(
            summary = "Get all hotels",
            description = """
                    Returns all hotels.
                    Logs TRACE, DEBUG and INFO with the result count.
                    """
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return ResponseEntity.ok(
                hotelService.getAllHotels()
        );
    }

    @Operation(
            summary = "Get hotel by ID",
            description = """
                    Returns a hotel by ID.
                    Logs INFO when found and WARN when not found.
                    """
    )
    @GetMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> getHotelById(
            @Parameter(
                    description = "Unique ID of the hotel",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId
    ) {
        Optional<Hotel> hotel =
                hotelService.getHotelById(hotelId);

        return hotel
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Search hotels by city",
            description = """
                    Returns hotels in the specified city.
                    Logs INFO when results exist and WARN when no hotel is found.
                    """
    )
    @GetMapping("/city")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Hotel>> getHotelsByCity(
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
            description = """
                    Searches hotels using a full or partial name.
                    Logs INFO when matches exist and WARN when no result is found.
                    """
    )
    @GetMapping("/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Hotel>> getHotelsByName(
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
            description = """
                    Updates an existing hotel.
                    Logs INFO on success, WARN when not found
                    and ERROR for invalid data.
                    """
    )
    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(
            @Parameter(
                    description = "ID of the hotel to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId,

            @Valid @RequestBody Hotel hotel
    ) {
        Hotel updatedHotel =
                hotelService.updateHotel(hotelId, hotel);

        if (updatedHotel == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedHotel);
    }

    @Operation(
            summary = "Delete a hotel",
            description = """
                    Deletes a hotel.
                    Logs INFO on success, ERROR when not found
                    and FATAL when database availability is false.
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
        boolean deleted =
                hotelService.deleteHotel(
                        hotelId,
                        dbAvailable
                );

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}