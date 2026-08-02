package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.repo.HotelRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private static final Logger log =
            LogManager.getLogger(HotelServiceImpl.class);

    private final HotelRepo hotelRepo;

    @Override
    public Hotel createHotel(Hotel hotel) {

        log.trace(
                "Entering createHotel() with name={}",
                hotel.getName()
        );

        log.debug(
                "Validating hotel data. name={}, city={}, stars={}",
                hotel.getName(),
                hotel.getCity(),
                hotel.getStarRating()
        );

        if (hotel.getName() == null
                || hotel.getName().isBlank()) {

            log.error(
                    "Hotel creation failed because name is missing"
            );

            throw new IllegalArgumentException(
                    "Hotel name is required"
            );
        }

        if (hotel.getCity() == null
                || hotel.getCity().isBlank()) {

            log.error(
                    "Hotel creation failed because city is missing"
            );

            throw new IllegalArgumentException(
                    "Hotel city is required"
            );
        }

        if (hotel.getRooms() != null) {
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }

            log.debug(
                    "Associated {} rooms with hotel={}",
                    hotel.getRooms().size(),
                    hotel.getName()
            );
        }

        Hotel savedHotel =
                hotelRepo.save(hotel);

        log.info(
                "Hotel created successfully. id={}, name={}",
                savedHotel.getId(),
                savedHotel.getName()
        );

        return savedHotel;
    }

    @Override
    public List<Hotel> getAllHotels() {

        log.trace("Entering getAllHotels()");
        log.debug("Retrieving all hotels");

        List<Hotel> hotels =
                hotelRepo.findAll();

        log.info(
                "Retrieved {} hotels",
                hotels.size()
        );

        return hotels;
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {

        log.trace(
                "Entering getHotelById() with id={}",
                hotelId
        );

        log.debug(
                "Searching hotel with id={}",
                hotelId
        );

        Optional<Hotel> hotel =
                hotelRepo.findById(hotelId);

        if (hotel.isEmpty()) {
            log.warn(
                    "Hotel with id={} was not found",
                    hotelId
            );
        } else {
            log.info(
                    "Hotel with id={} found successfully",
                    hotelId
            );
        }

        return hotel;
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {

        log.trace(
                "Entering getHotelsByCity() with city={}",
                city
        );

        log.debug(
                "Searching hotels by city={}",
                city
        );

        List<Hotel> hotels =
                hotelRepo.findByCityIgnoreCase(city);

        if (hotels.isEmpty()) {
            log.warn(
                    "No hotels found in city={}",
                    city
            );
        } else {
            log.info(
                    "Found {} hotels in city={}",
                    hotels.size(),
                    city
            );
        }

        return hotels;
    }

    @Override
    public List<Hotel> getHotelsByName(String name) {

        log.trace(
                "Entering getHotelsByName() with name={}",
                name
        );

        log.debug(
                "Searching hotels containing name={}",
                name
        );

        List<Hotel> hotels =
                hotelRepo.findByNameContainingIgnoreCase(
                        name
                );

        if (hotels.isEmpty()) {
            log.warn(
                    "No hotels matched name={}",
                    name
            );
        } else {
            log.info(
                    "Found {} hotels matching name={}",
                    hotels.size(),
                    name
            );
        }

        return hotels;
    }

    @Override
    public Hotel updateHotel(
            Long hotelId,
            Hotel hotel
    ) {
        log.trace(
                "Entering updateHotel() with id={}",
                hotelId
        );

        log.debug(
                "Validating hotel update for id={}",
                hotelId
        );

        Optional<Hotel> optionalHotel =
                hotelRepo.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            log.warn(
                    "Hotel update failed because id={} was not found",
                    hotelId
            );

            return null;
        }

        if (hotel.getName() == null
                || hotel.getName().isBlank()) {

            log.error(
                    "Hotel update failed because name is missing. id={}",
                    hotelId
            );

            throw new IllegalArgumentException(
                    "Hotel name is required"
            );
        }

        Hotel existingHotel =
                optionalHotel.get();

        existingHotel.setName(hotel.getName());
        existingHotel.setCity(hotel.getCity());
        existingHotel.setAddress(hotel.getAddress());
        existingHotel.setStarRating(
                hotel.getStarRating()
        );

        Hotel updatedHotel =
                hotelRepo.save(existingHotel);

        log.info(
                "Hotel updated successfully. id={}",
                hotelId
        );

        return updatedHotel;
    }

    @Override
    public boolean deleteHotel(
            Long hotelId,
            boolean dbAvailable
    ) {
        log.trace(
                "Entering deleteHotel() with id={}, dbAvailable={}",
                hotelId,
                dbAvailable
        );

        log.debug(
                "Preparing deletion for hotel id={}",
                hotelId
        );

        if (!dbAvailable) {
            log.fatal(
                    "Hotel deletion failed because database is unavailable. id={}",
                    hotelId
            );

            throw new IllegalStateException(
                    "Database is unavailable"
            );
        }

        if (!hotelRepo.existsById(hotelId)) {
            log.error(
                    "Hotel deletion failed because id={} does not exist",
                    hotelId
            );

            return false;
        }

        hotelRepo.deleteById(hotelId);

        log.info(
                "Hotel deleted successfully. id={}",
                hotelId
        );

        return true;
    }
}