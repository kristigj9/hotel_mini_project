package com.lhind.hotel.service;

import com.lhind.hotel.dto.GuestDto;

import java.util.List;

public interface GuestService {

    GuestDto createGuest(GuestDto guestDto);

    List<GuestDto> getAllGuests();

    GuestDto getGuestById(Long guestId);

    GuestDto getGuestByEmail(String email);

    GuestDto updateGuest(
            Long guestId,
            GuestDto guestDto
    );

    void deleteGuest(Long guestId);
}