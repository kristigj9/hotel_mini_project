package com.lhind.hotel.service;

import com.lhind.hotel.dto.GuestProfileDto;

import java.util.List;

public interface GuestProfileService {

    GuestProfileDto createGuestProfile(
            GuestProfileDto guestProfileDto
    );

    List<GuestProfileDto> getAllGuestProfiles();

    GuestProfileDto getGuestProfileById(Long profileId);

    GuestProfileDto getGuestProfileByGuestId(Long guestId);

    GuestProfileDto updateGuestProfile(
            Long profileId,
            GuestProfileDto guestProfileDto
    );

    void deleteGuestProfile(Long profileId);
}