package com.lhind.hotel.dto;

import com.lhind.hotel.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusUpdateDto {

    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
}