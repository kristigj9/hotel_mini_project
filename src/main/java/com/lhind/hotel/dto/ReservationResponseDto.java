package com.lhind.hotel.dto;

import com.lhind.hotel.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private Long id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private Integer numberOfGuests;

    private BigDecimal totalPrice;

    private ReservationStatus status;

    private LocalDateTime createdAt;

    private Long guestId;

    private String guestFullName;

    private Long roomId;

    private String roomNumber;

    private Long hotelId;

    private String hotelName;
}