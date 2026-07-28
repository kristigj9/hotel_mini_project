package com.lhind.hotel.dto.response;


public record TokenResponse(
        String token,
        String type
) {
}