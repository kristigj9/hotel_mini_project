package com.lhind.hotel.service;

import com.lhind.hotel.dto.HotelDto;
import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.exception.ResourceNotFoundException;
import com.lhind.hotel.mapper.HotelMapper;
import com.lhind.hotel.repo.HotelRepo;
import com.lhind.hotel.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final Logger log =
            LogManager.getLogger(HotelServiceImpl.class);

    private final HotelRepo hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {

        log.debug(
                "Creating hotel with name={} and city={}",
                hotelDto.getName(),
                hotelDto.getCity()
        );

        Hotel hotel = hotelMapper.toEntity(hotelDto);

        hotel.setId(null);

        Hotel savedHotel =
                hotelRepository.save(hotel);

        log.info(
                "Hotel created successfully. id={}, name={}",
                savedHotel.getId(),
                savedHotel.getName()
        );

        return hotelMapper.toDto(savedHotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getAllHotels() {

        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HotelDto getHotelById(Long hotelId) {

        Hotel hotel = findHotelById(hotelId);

        return hotelMapper.toDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByCity(String city) {

        return hotelRepository
                .findByNameContainingIgnoreCase(city)
                .stream()
                .map(hotelMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotelDto> getHotelsByName(String name) {

        return hotelRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(hotelMapper::toDto)
                .toList();
    }

    @Override
    public HotelDto updateHotel(
            Long hotelId,
            HotelDto hotelDto
    ) {

        Hotel existingHotel =
                findHotelById(hotelId);

        hotelMapper.updateEntity(
                existingHotel,
                hotelDto
        );

        Hotel updatedHotel =
                hotelRepository.save(existingHotel);

        log.info(
                "Hotel updated successfully. id={}",
                updatedHotel.getId()
        );

        return hotelMapper.toDto(updatedHotel);
    }

    @Override
    public void deleteHotel(
            Long hotelId,
            boolean dbAvailable
    ) {

        log.debug(
                "Deleting hotel with id={}",
                hotelId
        );

        if (!dbAvailable) {
            log.error(
                    "Database is unavailable while deleting hotel id={}",
                    hotelId
            );

            throw new IllegalStateException(
                    "Database is currently unavailable"
            );
        }

        Hotel hotel = findHotelById(hotelId);

        hotelRepository.delete(hotel);

        log.info(
                "Hotel deleted successfully. id={}",
                hotelId
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