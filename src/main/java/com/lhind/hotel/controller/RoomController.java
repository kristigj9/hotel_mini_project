package com.lhind.hotel.controller;

import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.service.RoomService;
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
@RequestMapping("/api/rooms")
@Tag(
        name = "Rooms",
        description = "Room Management — demonstrates Log4j2 levels TRACE → FATAL"
)
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(
            summary = "Create a room",
            description = """
                    Creates a room for an existing hotel.
                    Logs INFO on success, WARN when the hotel does not exist
                    and ERROR when the room number already exists.
                    """
    )
    @PostMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Room> createRoom(
            @Parameter(
                    description = "ID of the hotel",
                    example = "1",
                    required = true
            )
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

    @Operation(
            summary = "Get all rooms",
            description = "Returns all rooms and logs the number of results"
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }

    @Operation(
            summary = "Get room by ID",
            description = "Logs INFO when found and WARN when not found"
    )
    @GetMapping("/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Room> getRoomById(
            @Parameter(
                    description = "Unique ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId
    ) {
        Optional<Room> room =
                roomService.getRoomById(roomId);

        return room
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @Operation(
            summary = "Get rooms by hotel",
            description = "Returns all rooms associated with a hotel"
    )
    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Room>> getRoomsByHotelId(
            @Parameter(
                    description = "ID of the hotel",
                    example = "1",
                    required = true
            )
            @PathVariable Long hotelId
    ) {
        return ResponseEntity.ok(
                roomService.getRoomsByHotelId(hotelId)
        );
    }

    @Operation(
            summary = "Get rooms by status",
            description = "Returns rooms that match the supplied status"
    )
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<Room>> getRoomsByStatus(
            @Parameter(
                    description = "Room status",
                    example = "AVAILABLE",
                    required = true
            )
            @PathVariable RoomStatus status
    ) {
        return ResponseEntity.ok(
                roomService.getRoomsByStatus(status)
        );
    }

    @Operation(
            summary = "Update a room",
            description = """
                    Updates room information.
                    Logs INFO on success, WARN when not found
                    and ERROR when the room number conflicts.
                    """
    )
    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(
            @Parameter(
                    description = "ID of the room to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId,

            @Valid @RequestBody Room room
    ) {
        Room updatedRoom =
                roomService.updateRoom(roomId, room);

        if (updatedRoom == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .build();
        }

        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(
            summary = "Update room status",
            description = "Updates only the status of an existing room"
    )
    @PatchMapping("/{roomId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<Room> updateRoomStatus(
            @Parameter(
                    description = "ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId,

            @Parameter(
                    description = "New room status",
                    example = "OCCUPIED",
                    required = true
            )
            @RequestParam RoomStatus status
    ) {
        Room updatedRoom =
                roomService.updateRoomStatus(roomId, status);

        if (updatedRoom == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(
            summary = "Delete a room",
            description = "Logs INFO on success and ERROR when not found"
    )
    @DeleteMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(
            @Parameter(
                    description = "ID of the room to delete",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId
    ) {
        boolean deleted =
                roomService.deleteRoom(roomId);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}