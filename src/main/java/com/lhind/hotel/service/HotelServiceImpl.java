package com.lhind.hotel.service;

import com.lhind.hotel.entity.Hotel;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.repo.HotelRepo;
import com.lhind.hotel.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepo hotelRepo;

    public HotelServiceImpl(HotelRepo hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public Hotel createHotel(Hotel hotel) {

        if (hotel.getRooms() != null) {
            for (Room room : hotel.getRooms()) {
                room.setHotel(hotel);
            }
        }

        return hotelRepo.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepo.findAll();
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return hotelRepo.findById(hotelId);
    }

    @Override
    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepo.findByCityIgnoreCase(city);
    }

    @Override
    public List<Hotel> getHotelsByName(String name) {
        return hotelRepo.findByNameContainingIgnoreCase(name);
    }
    @Override
    public Hotel updateHotel(Long hotelId, Hotel hotel) {

        Optional<Hotel> optionalHotel = hotelRepo.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            return null;
        }

        Hotel existingHotel = optionalHotel.get();

        existingHotel.setName(hotel.getName());
        existingHotel.setCity(hotel.getCity());
        existingHotel.setAddress(hotel.getAddress());
        existingHotel.setStarRating(hotel.getStarRating());

        return hotelRepo.save(existingHotel);
    }

    @Override
    public boolean deleteHotel(Long hotelId) {

        Optional<Hotel> optionalHotel = hotelRepo.findById(hotelId);

        if (optionalHotel.isEmpty()) {
            return false;
        }

        hotelRepo.delete(optionalHotel.get());
        return true;
    }
}