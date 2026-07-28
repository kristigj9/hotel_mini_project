package com.lhind.hotel.repo;

import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepo extends JpaRepository<Room, Long> {

    List<Room> findByHotel_Id(Long hotelId);

    List<Room> findByStatus(RoomStatus status);

    Optional<Room> findByRoomNumberAndHotel_Id(
            String roomNumber,
            Long hotelId
    );
}