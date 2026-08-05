package com.lhind.hotel.mapper;

import com.lhind.hotel.dto.RoomDto;
import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(
            RoomDto dto,
            Hotel hotel
    ) {
        if (dto == null) {
            return null;
        }

        Room room = new Room();

        room.setId(dto.getId());
        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());
        room.setStatus(dto.getStatus());
        room.setHotel(hotel);

        return room;
    }

    public RoomDto toDto(Room room) {
        if (room == null) {
            return null;
        }

        RoomDto dto = new RoomDto();

        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setCapacity(room.getCapacity());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setStatus(room.getStatus());

        if (room.getHotel() != null) {
            dto.setHotelId(room.getHotel().getId());
        }

        return dto;
    }

    public void updateEntity(
            Room room,
            RoomDto dto,
            Hotel hotel
    ) {
        if (room == null || dto == null) {
            return;
        }

        room.setRoomNumber(dto.getRoomNumber());
        room.setRoomType(dto.getRoomType());
        room.setCapacity(dto.getCapacity());
        room.setPricePerNight(dto.getPricePerNight());
        room.setStatus(dto.getStatus());
        room.setHotel(hotel);
    }
}