package com.lhind.hotel.dto;


import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.entity.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Long id;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Room capacity is required")
    @Positive(message = "Room capacity must be greater than zero")
    private Integer capacity;
    @NotNull(message = "Price per night is required")
    @Positive(message = "Price per night must be greater than zero")
    private BigDecimal pricePerNight;
    @NotNull(message = "Room status is required")
    private RoomStatus status;
    private Long hotelId;
}