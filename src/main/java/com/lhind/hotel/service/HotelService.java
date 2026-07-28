package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    Optional<Hotel> getHotelById(Long hotelId);
    List<Hotel> getHotelsByName(String name);
    List<Hotel> getHotelsByCity(String city);
    Hotel updateHotel(Long hotelId, Hotel hotel);

    boolean deleteHotel(Long hotelId);
}