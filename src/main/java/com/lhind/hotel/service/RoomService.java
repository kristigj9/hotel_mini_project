package com.lhind.hotel.service;

import com.lhind.hotel.dto.RoomDto;
import com.lhind.hotel.dto.RoomStatusUpdateDto;
import com.lhind.hotel.entity.RoomStatus;

import java.util.List;

public interface RoomService {

    RoomDto createRoom(RoomDto roomDto);

    List<RoomDto> getAllRooms();

    RoomDto getRoomById(Long roomId);

    List<RoomDto> getRoomsByHotelId(Long hotelId);

    List<RoomDto> getRoomsByStatus(RoomStatus status);

    RoomDto updateRoom(
            Long roomId,
            RoomDto roomDto
    );

    RoomDto updateRoomStatus(
            Long roomId,
            RoomStatusUpdateDto statusDto
    );

    void deleteRoom(Long roomId);
}