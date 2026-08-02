package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    Optional<Hotel> getHotelById(Long hotelId);

    List<Hotel> getHotelsByCity(String city);

    List<Hotel> getHotelsByName(String name);

    Hotel updateHotel(Long hotelId, Hotel hotel);

    boolean deleteHotel(
            Long hotelId,
            boolean dbAvailable
    );
}