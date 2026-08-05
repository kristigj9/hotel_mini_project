package com.lhind.hotel.mapper;

import com.lhind.hotel.dto.GuestDto;
import com.lhind.hotel.entity.Guest;
import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public Guest toEntity(GuestDto dto) {
        if (dto == null) {
            return null;
        }

        Guest guest = new Guest();

        guest.setId(dto.getId());
        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setPhoneNumber(dto.getPhoneNumber());

        return guest;
    }

    public GuestDto toDto(Guest guest) {
        if (guest == null) {
            return null;
        }

        GuestDto dto = new GuestDto();

        dto.setId(guest.getId());
        dto.setFirstName(guest.getFirstName());
        dto.setLastName(guest.getLastName());
        dto.setEmail(guest.getEmail());
        dto.setPhoneNumber(guest.getPhoneNumber());

        return dto;
    }

    public void updateEntity(Guest guest, GuestDto dto) {
        if (guest == null || dto == null) {
            return;
        }

        guest.setFirstName(dto.getFirstName());
        guest.setLastName(dto.getLastName());
        guest.setEmail(dto.getEmail());
        guest.setPhoneNumber(dto.getPhoneNumber());
    }
}