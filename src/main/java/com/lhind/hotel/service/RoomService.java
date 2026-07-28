package com.lhind.hotel.service;

import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;

import java.util.List;
import java.util.Optional;

public interface RoomService {

    Room createRoom(Long hotelId, Room room);

    List<Room> getAllRooms();

    Optional<Room> getRoomById(Long roomId);

    List<Room> getRoomsByHotelId(Long hotelId);

    List<Room> getRoomsByStatus(RoomStatus status);

    Room updateRoom(Long roomId, Room room);

    Room updateRoomStatus(Long roomId, RoomStatus status);

    boolean deleteRoom(Long roomId);
}