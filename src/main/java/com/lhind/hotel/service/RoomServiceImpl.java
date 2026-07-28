package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.repo.HotelRepo;
import com.lhind.hotel.repo.RoomRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;
    private final HotelRepo hotelRepo;

    public RoomServiceImpl(
            RoomRepo roomRepo,
            HotelRepo hotelRepo
    ) {
        this.roomRepo = roomRepo;
        this.hotelRepo = hotelRepo;
    }

    @Override
    public Room createRoom(Long hotelId, Room room) {

        Optional<Hotel> optionalHotel = hotelRepo.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            return null;
        }

        Optional<Room> existingRoom =
                roomRepo.findByRoomNumberAndHotel_Id(
                        room.getRoomNumber(),
                        hotelId
                );

        if (existingRoom.isPresent()) {
            return null;
        }

        Hotel hotel = optionalHotel.get();
        room.setHotel(hotel);

        return roomRepo.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepo.findById(roomId);
    }

    @Override
    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepo.findByHotel_Id(hotelId);
    }

    @Override
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return roomRepo.findByStatus(status);
    }

    @Override
    public Room updateRoom(Long roomId, Room room) {

        Optional<Room> optionalRoom = roomRepo.findById(roomId);

        if (optionalRoom.isEmpty()) {
            return null;
        }

        Room existingRoom = optionalRoom.get();

        Optional<Room> roomWithSameNumber =
                roomRepo.findByRoomNumberAndHotel_Id(
                        room.getRoomNumber(),
                        existingRoom.getHotel().getId()
                );

        if (roomWithSameNumber.isPresent()
                && !roomWithSameNumber.get().getId().equals(roomId)) {
            return null;
        }

        existingRoom.setRoomNumber(room.getRoomNumber());
        existingRoom.setCapacity(room.getCapacity());
        existingRoom.setPricePerNight(room.getPricePerNight());
        existingRoom.setRoomType(room.getRoomType());

        if (room.getStatus() != null) {
            existingRoom.setStatus(room.getStatus());
        }

        return roomRepo.save(existingRoom);
    }

    @Override
    public Room updateRoomStatus(
            Long roomId,
            RoomStatus status
    ) {
        Optional<Room> optionalRoom = roomRepo.findById(roomId);

        if (optionalRoom.isEmpty()) {
            return null;
        }

        Room existingRoom = optionalRoom.get();
        existingRoom.setStatus(status);

        return roomRepo.save(existingRoom);
    }

    @Override
    public boolean deleteRoom(Long roomId) {

        if (!roomRepo.existsById(roomId)) {
            return false;
        }

        roomRepo.deleteById(roomId);
        return true;
    }
}