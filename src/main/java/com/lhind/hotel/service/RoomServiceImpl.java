package com.lhind.hotel.service;

import com.lhind.hotel.dto.RoomDto;
import com.lhind.hotel.dto.RoomStatusUpdateDto;
import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.entity.RoomStatus;
import com.lhind.hotel.exception.ResourceNotFoundException;
import com.lhind.hotel.mapper.RoomMapper;
import com.lhind.hotel.repo.HotelRepo;
import com.lhind.hotel.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepository;
    private final HotelRepo hotelRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomDto createRoom(RoomDto roomDto) {

        Hotel hotel =
                findHotelById(roomDto.getHotelId());

        if (roomRepository.existsByRoomNumberAndHotel_Id(
                roomDto.getRoomNumber(),
                hotel.getId()
        )) {
            throw new IllegalArgumentException(
                    "Room number " +
                            roomDto.getRoomNumber() +
                            " already exists in hotel id: " +
                            hotel.getId()
            );
        }

        Room room =
                roomMapper.toEntity(roomDto, hotel);

        room.setId(null);

        Room savedRoom =
                roomRepository.save(room);

        return roomMapper.toDto(savedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {

        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long roomId) {

        Room room = findRoomById(roomId);

        return roomMapper.toDto(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByHotelId(
            Long hotelId
    ) {

        if (!hotelRepository.existsById(hotelId)) {
            throw new ResourceNotFoundException(
                    "Hotel not found with id: " +
                            hotelId
            );
        }

        return roomRepository.findByHotel_Id(hotelId)
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getRoomsByStatus(
            RoomStatus status
    ) {

        return roomRepository.findByStatus(status)
                .stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    public RoomDto updateRoom(
            Long roomId,
            RoomDto roomDto
    ) {

        Room existingRoom =
                findRoomById(roomId);

        Hotel hotel =
                findHotelById(roomDto.getHotelId());

        roomRepository
                .findByRoomNumberAndHotel_Id(
                        roomDto.getRoomNumber(),
                        hotel.getId()
                )
                .filter(room ->
                        !room.getId().equals(roomId)
                )
                .ifPresent(room -> {
                    throw new IllegalArgumentException(
                            "Room number " +
                                    roomDto.getRoomNumber() +
                                    " already exists in hotel id: " +
                                    hotel.getId()
                    );
                });

        roomMapper.updateEntity(
                existingRoom,
                roomDto,
                hotel
        );

        Room updatedRoom =
                roomRepository.save(existingRoom);

        return roomMapper.toDto(updatedRoom);
    }

    @Override
    public RoomDto updateRoomStatus(
            Long roomId,
            RoomStatusUpdateDto statusDto
    ) {

        Room room = findRoomById(roomId);

        room.setStatus(statusDto.getStatus());

        Room updatedRoom =
                roomRepository.save(room);

        return roomMapper.toDto(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {

        Room room = findRoomById(roomId);

        roomRepository.delete(room);
    }

    private Room findRoomById(Long roomId) {

        return roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " +
                                        roomId
                        )
                );
    }

    private Hotel findHotelById(Long hotelId) {

        return hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with id: " +
                                        hotelId
                        )
                );
    }
}