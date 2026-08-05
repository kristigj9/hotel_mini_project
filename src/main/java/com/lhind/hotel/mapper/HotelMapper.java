package com.lhind.hotel.mapper;

import com.lhind.hotel.dto.HotelDto;
import com.lhind.hotel.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelDto dto) {
        if (dto == null) {
            return null;
        }

        Hotel hotel = new Hotel();

        hotel.setId(dto.getId());
        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setAddress(dto.getAddress());
        hotel.setStarRating(dto.getStarRating());

        return hotel;
    }

    public HotelDto toDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        HotelDto dto = new HotelDto();

        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setCity(hotel.getCity());
        dto.setAddress(hotel.getAddress());
        dto.setStarRating(hotel.getStarRating());

        return dto;
    }

    public void updateEntity(Hotel hotel, HotelDto dto) {
        if (hotel == null || dto == null) {
            return;
        }

        hotel.setName(dto.getName());
        hotel.setCity(dto.getCity());
        hotel.setAddress(dto.getAddress());
        hotel.setStarRating(dto.getStarRating());
    }
}