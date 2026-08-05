package com.lhind.hotel.service;
import com.lhind.hotel.dto.GuestDto;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.exception.ResourceNotFoundException;
import com.lhind.hotel.mapper.GuestMapper;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestServiceImpl implements GuestService {

    private final GuestRepo guestRepository;
    private final GuestMapper guestMapper;

    @Override
    public GuestDto createGuest(GuestDto guestDto) {

        if (guestRepository.existsByEmail(guestDto.getEmail())) {
            throw new IllegalArgumentException(
                    "A guest with email " +
                            guestDto.getEmail() +
                            " already exists"
            );
        }

        Guest guest = guestMapper.toEntity(guestDto);

        // ID-ja duhet të gjenerohet nga databaza.
        guest.setId(null);

        Guest savedGuest = guestRepository.save(guest);

        return guestMapper.toDto(savedGuest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestDto> getAllGuests() {

        return guestRepository.findAll()
                .stream()
                .map(guestMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GuestDto getGuestById(Long guestId) {

        Guest guest = findGuestById(guestId);

        return guestMapper.toDto(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestDto getGuestByEmail(String email) {

        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with email: " + email
                        )
                );

        return guestMapper.toDto(guest);
    }

    @Override
    public GuestDto updateGuest(
            Long guestId,
            GuestDto guestDto
    ) {

        Guest existingGuest = findGuestById(guestId);

        guestRepository.findByEmail(guestDto.getEmail())
                .filter(guest ->
                        !guest.getId().equals(guestId)
                )
                .ifPresent(guest -> {
                    throw new IllegalArgumentException(
                            "A guest with email " +
                                    guestDto.getEmail() +
                                    " already exists"
                    );
                });

        guestMapper.updateEntity(
                existingGuest,
                guestDto
        );

        Guest updatedGuest =
                guestRepository.save(existingGuest);

        return guestMapper.toDto(updatedGuest);
    }

    @Override
    public void deleteGuest(Long guestId) {

        Guest guest = findGuestById(guestId);

        guestRepository.delete(guest);
    }

    private Guest findGuestById(Long guestId) {

        return guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: " + guestId
                        )
                );
    }
}