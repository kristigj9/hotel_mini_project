package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // Krijon një hotel
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Hotel> createHotel(
            @Valid @RequestBody Hotel hotel
    ) {
        Hotel createdHotel = hotelService.createHotel(hotel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    // Kthen të gjitha hotelet
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<Hotel>> getAllHotels() {

        List<Hotel> hotels = hotelService.getAllHotels();

        return ResponseEntity.ok(hotels);
    }

    // Kthen hotelin sipas ID-së
    @GetMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Hotel> getHotelById(
            @PathVariable Long hotelId
    ) {
        Optional<Hotel> optionalHotel =
                hotelService.getHotelById(hotelId);

        if (optionalHotel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalHotel.get());
    }

    // Kërkon hotelet sipas qytetit
    @GetMapping("/city")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<List<Hotel>> getHotelsByCity(
            @RequestParam String city
    ) {
        List<Hotel> hotels =
                hotelService.getHotelsByCity(city);

        return ResponseEntity.ok(hotels);
    }

    // Përditëson hotelin
    @PutMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Hotel> updateHotel(
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

    // Fshin hotelin
    @DeleteMapping("/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> deleteHotel(
            @PathVariable Long hotelId
    ) {
        boolean deleted = hotelService.deleteHotel(hotelId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}