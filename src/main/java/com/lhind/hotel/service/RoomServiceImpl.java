package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.repo.HotelRepo;
import com.lhind.hotel.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private static final Logger log =
            LogManager.getLogger(RoomServiceImpl.class);

    private final RoomRepo roomRepo;
    private final HotelRepo hotelRepo;

    @Override
    public Room createRoom(
            Long hotelId,
            Room room
    ) {
        log.trace(
                "Entering createRoom() with hotelId={}, roomNumber={}",
                hotelId,
                room.getRoomNumber()
        );

        log.debug(
                "Checking hotel existence for hotelId={}",
                hotelId
        );

        Optional<Hotel> optionalHotel =
                hotelRepo.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            log.warn(
                    "Room creation failed because hotelId={} was not found",
                    hotelId
            );

            return null;
        }

        Optional<Room> existingRoom =
                roomRepo.findByRoomNumberAndHotel_Id(
                        room.getRoomNumber(),
                        hotelId
                );

        if (existingRoom.isPresent()) {
            log.error(
                    "Room creation failed because number={} already exists in hotelId={}",
                    room.getRoomNumber(),
                    hotelId
            );

            return null;
        }

        room.setHotel(optionalHotel.get());

        Room savedRoom =
                roomRepo.save(room);

        log.info(
                "Room created successfully. id={}, number={}, hotelId={}",
                savedRoom.getId(),
                savedRoom.getRoomNumber(),
                hotelId
        );

        return savedRoom;
    }

    @Override
    public List<Room> getAllRooms() {

        log.trace("Entering getAllRooms()");
        log.debug("Retrieving all rooms");

        List<Room> rooms =
                roomRepo.findAll();

        log.info(
                "Retrieved {} rooms",
                rooms.size()
        );

        return rooms;
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {

        log.trace(
                "Entering getRoomById() with id={}",
                roomId
        );

        Optional<Room> room =
                roomRepo.findById(roomId);

        if (room.isEmpty()) {
            log.warn(
                    "Room with id={} was not found",
                    roomId
            );
        } else {
            log.info(
                    "Room with id={} found successfully",
                    roomId
            );
        }

        return room;
    }

    @Override
    public List<Room> getRoomsByHotelId(Long hotelId) {

        log.trace(
                "Entering getRoomsByHotelId() with hotelId={}",
                hotelId
        );

        List<Room> rooms =
                roomRepo.findByHotel_Id(hotelId);

        if (rooms.isEmpty()) {
            log.warn(
                    "No rooms found for hotelId={}",
                    hotelId
            );
        } else {
            log.info(
                    "Retrieved {} rooms for hotelId={}",
                    rooms.size(),
                    hotelId
            );
        }

        return rooms;
    }

    @Override
    public List<Room> getRoomsByStatus(
            RoomStatus status
    ) {
        log.trace(
                "Entering getRoomsByStatus() with status={}",
                status
        );

        List<Room> rooms =
                roomRepo.findByStatus(status);

        if (rooms.isEmpty()) {
            log.warn(
                    "No rooms found with status={}",
                    status
            );
        } else {
            log.info(
                    "Retrieved {} rooms with status={}",
                    rooms.size(),
                    status
            );
        }

        return rooms;
    }

    @Override
    public Room updateRoom(
            Long roomId,
            Room room
    ) {
        log.trace(
                "Entering updateRoom() with roomId={}",
                roomId
        );

        log.debug(
                "Validating room update for roomId={}",
                roomId
        );

        Optional<Room> optionalRoom =
                roomRepo.findById(roomId);

        if (optionalRoom.isEmpty()) {
            log.warn(
                    "Room update failed because roomId={} was not found",
                    roomId
            );

            return null;
        }

        Room existingRoom =
                optionalRoom.get();

        Optional<Room> sameNumber =
                roomRepo.findByRoomNumberAndHotel_Id(
                        room.getRoomNumber(),
                        existingRoom.getHotel().getId()
                );

        if (sameNumber.isPresent()
                && !sameNumber.get()
                .getId()
                .equals(roomId)) {

            log.error(
                    "Room update failed because number={} already exists in hotelId={}",
                    room.getRoomNumber(),
                    existingRoom.getHotel().getId()
            );

            return null;
        }

        existingRoom.setRoomNumber(
                room.getRoomNumber()
        );
        existingRoom.setCapacity(
                room.getCapacity()
        );
        existingRoom.setPricePerNight(
                room.getPricePerNight()
        );
        existingRoom.setRoomType(
                room.getRoomType()
        );

        if (room.getStatus() != null) {
            existingRoom.setStatus(
                    room.getStatus()
            );
        }

        Room updatedRoom =
                roomRepo.save(existingRoom);

        log.info(
                "Room updated successfully. id={}",
                roomId
        );

        return updatedRoom;
    }

    @Override
    public Room updateRoomStatus(
            Long roomId,
            RoomStatus status
    ) {
        log.trace(
                "Entering updateRoomStatus() with roomId={}, status={}",
                roomId,
                status
        );

        Optional<Room> optionalRoom =
                roomRepo.findById(roomId);

        if (optionalRoom.isEmpty()) {
            log.warn(
                    "Room status update failed because roomId={} was not found",
                    roomId
            );

            return null;
        }

        Room existingRoom =
                optionalRoom.get();

        existingRoom.setStatus(status);

        Room updatedRoom =
                roomRepo.save(existingRoom);

        log.info(
                "Room status updated successfully. id={}, status={}",
                roomId,
                status
        );

        return updatedRoom;
    }

    @Override
    public boolean deleteRoom(Long roomId) {

        log.trace(
                "Entering deleteRoom() with roomId={}",
                roomId
        );

        if (!roomRepo.existsById(roomId)) {
            log.error(
                    "Room deletion failed because roomId={} does not exist",
                    roomId
            );

            return false;
        }

        roomRepo.deleteById(roomId);

        log.info(
                "Room deleted successfully. id={}",
                roomId
        );

        return true;
    }
}