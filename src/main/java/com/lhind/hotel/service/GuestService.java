package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;

import java.util.List;
import java.util.Optional;

public interface GuestService {

    Guest createGuest(Guest guest);

    List<Guest> getAllGuests();

    Optional<Guest> getGuestById(Long guestId);

    Optional<Guest> getGuestByEmail(String email);

    Guest updateGuest(Long guestId, Guest guest);

    boolean deleteGuest(Long guestId);
}