package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Krijon një dhomë për një hotel
    @PostMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<Room> createRoom(
            @PathVariable Long hotelId,
            @Valid @RequestBody Room room
    ) {
        Room createdRoom =
                roomService.createRoom(hotelId, room);

        if (createdRoom == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRoom);
    }

    // Kthen të gjitha dhomat
    @GetMapping
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<List<Room>> getAllRooms() {

        List<Room> rooms = roomService.getAllRooms();

        return ResponseEntity.ok(rooms);
    }

    // Kthen dhomën sipas ID-së
    @GetMapping("/{roomId}")
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<Room> getRoomById(
            @PathVariable Long roomId
    ) {
        Optional<Room> optionalRoom =
                roomService.getRoomById(roomId);

        if (optionalRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalRoom.get());
    }

    // Kthen dhomat e një hoteli
    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<Room>> getRoomsByHotelId(
            @PathVariable Long hotelId
    ) {
        List<Room> rooms =
                roomService.getRoomsByHotelId(hotelId);

        return ResponseEntity.ok(rooms);
    }

    // Kthen dhomat sipas statusit
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<List<Room>> getRoomsByStatus(
            @PathVariable RoomStatus status
    ) {
        List<Room> rooms =
                roomService.getRoomsByStatus(status);

        return ResponseEntity.ok(rooms);
    }

    // Përditëson të dhënat e dhomës
    @PutMapping("/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long roomId,
            @Valid @RequestBody Room room
    ) {
        if (roomService.getRoomById(roomId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Room updatedRoom =
                roomService.updateRoom(roomId, room);

        if (updatedRoom == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        return ResponseEntity.ok(updatedRoom);
    }

    // Përditëson vetëm statusin e dhomës
    @PatchMapping("/{roomId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")

    public ResponseEntity<Room> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam RoomStatus status
    ) {
        Room updatedRoom =
                roomService.updateRoomStatus(roomId, status);

        if (updatedRoom == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRoom);
    }

    // Fshin dhomën
    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasAnyRole('ADMIN')")

    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long roomId
    ) {
        boolean deleted = roomService.deleteRoom(roomId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}