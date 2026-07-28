package com.lhind.hotel.repo;
import com.lhind.hotel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Long> {

    // Kërkon hotelet sipas qytetit, pa dalluar shkronjat
    // e mëdha nga të voglat.
    List<Hotel> findByCityIgnoreCase(String city);
    List<Hotel> findByNameContainingIgnoreCase(String name);}
