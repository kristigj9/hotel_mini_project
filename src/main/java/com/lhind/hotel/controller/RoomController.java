package com.lhind.hotel.controller;

import com.lhind.hotel.dto.RoomDto;
import com.lhind.hotel.dto.RoomStatusUpdateDto;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.service.RoomService;
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
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(
        name = "Rooms",
        description = "Endpoints for managing hotel rooms"
)
public class RoomController {

    private final RoomService roomService;

    @Operation(
            summary = "Create a room",
            description = """
                    Creates a room for an existing hotel.
                    The hotel ID must be supplied in the request body.
                    """
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<RoomDto> createRoom(
            @Valid @RequestBody RoomDto roomDto
    ) {

        RoomDto createdRoom =
                roomService.createRoom(roomDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRoom);
    }

    @Operation(
            summary = "Get all rooms",
            description = "Returns all hotel rooms."
    )
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RoomDto>> getAllRooms() {

        return ResponseEntity.ok(
                roomService.getAllRooms()
        );
    }

    @Operation(
            summary = "Get room by ID",
            description = "Returns a room using its ID."
    )
    @GetMapping("/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoomDto> getRoomById(
            @Parameter(
                    description = "Unique ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId
    ) {

        RoomDto room =
                roomService.getRoomById(roomId);

        return ResponseEntity.ok(room);
    }

    @Operation(
            summary = "Get rooms by hotel",
            description = "Returns all rooms associated with a hotel."
    )
    @GetMapping("/hotel/{hotelId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<RoomDto>>
    getRoomsByHotelId(
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
            description = "Returns all rooms matching the supplied status."
    )
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<RoomDto>>
    getRoomsByStatus(
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
            description = "Updates an existing room."
    )
    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(
            @Parameter(
                    description = "ID of the room to update",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId,

            @Valid @RequestBody RoomDto roomDto
    ) {

        RoomDto updatedRoom =
                roomService.updateRoom(
                        roomId,
                        roomDto
                );

        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(
            summary = "Update room status",
            description = "Updates only the status of an existing room."
    )
    @PatchMapping("/{roomId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<RoomDto> updateRoomStatus(
            @Parameter(
                    description = "ID of the room",
                    example = "1",
                    required = true
            )
            @PathVariable Long roomId,

            @Valid @RequestBody
            RoomStatusUpdateDto statusDto
    ) {

        RoomDto updatedRoom =
                roomService.updateRoomStatus(
                        roomId,
                        statusDto
                );

        return ResponseEntity.ok(updatedRoom);
    }

    @Operation(
            summary = "Delete a room",
            description = "Deletes an existing room."
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

        roomService.deleteRoom(roomId);

        return ResponseEntity.noContent().build();
    }
}