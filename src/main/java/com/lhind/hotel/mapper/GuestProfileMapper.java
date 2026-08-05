package com.lhind.hotel.mapper;

import com.lhind.hotel.dto.GuestProfileDto;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.GuestProfile;
import org.springframework.stereotype.Component;

@Component
public class GuestProfileMapper {

    public GuestProfile toEntity(
            GuestProfileDto dto,
            Guest guest
    ) {
        if (dto == null) {
            return null;
        }

        GuestProfile profile = new GuestProfile();

        profile.setId(dto.getId());
        profile.setAddress(dto.getAddress());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setNationality(dto.getNationality());
        profile.setPreferredLanguage(dto.getPreferredLanguage());
        profile.setGuest(guest);

        return profile;
    }

    public GuestProfileDto toDto(GuestProfile profile) {
        if (profile == null) {
            return null;
        }

        GuestProfileDto dto = new GuestProfileDto();

        dto.setId(profile.getId());
        dto.setAddress(profile.getAddress());
        dto.setDateOfBirth(profile.getDateOfBirth());
        dto.setNationality(profile.getNationality());
        dto.setPreferredLanguage(profile.getPreferredLanguage());

        if (profile.getGuest() != null) {
            dto.setGuestId(profile.getGuest().getId());
        }

        return dto;
    }

    public void updateEntity(
            GuestProfile profile,
            GuestProfileDto dto,
            Guest guest
    ) {
        if (profile == null || dto == null) {
            return;
        }

        profile.setAddress(dto.getAddress());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setNationality(dto.getNationality());
        profile.setPreferredLanguage(dto.getPreferredLanguage());
        profile.setGuest(guest);
    }
}