package com.lhind.hotel.service;

import com.lhind.hotel.dto.HotelDto;

import java.util.List;

public interface HotelService {

    HotelDto createHotel(HotelDto hotelDto);

    List<HotelDto> getAllHotels();

    HotelDto getHotelById(Long hotelId);

    List<HotelDto> getHotelsByCity(String city);

    List<HotelDto> getHotelsByName(String name);

    HotelDto updateHotel(
            Long hotelId,
            HotelDto hotelDto
    );

    void deleteHotel(
            Long hotelId,
            boolean dbAvailable
    );
}