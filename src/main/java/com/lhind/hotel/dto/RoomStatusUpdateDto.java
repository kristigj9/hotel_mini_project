package com.lhind.hotel.dto;

import com.lhind.hotel.entity.RoomStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusUpdateDto {

    @NotNull(message = "Room status is required")
    private RoomStatus status;
}