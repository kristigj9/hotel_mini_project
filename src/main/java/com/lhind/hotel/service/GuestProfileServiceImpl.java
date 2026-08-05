package com.lhind.hotel.service.impl;

import com.lhind.hotel.dto.GuestProfileDto;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.GuestProfile;
import com.lhind.hotel.exception.ResourceNotFoundException;
import com.lhind.hotel.mapper.GuestProfileMapper;
import com.lhind.hotel.repo.GuestProfileRepo;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.service.GuestProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestProfileServiceImpl
        implements GuestProfileService {

    private final GuestProfileRepo guestProfileRepository;
    private final GuestRepo guestRepository;
    private final GuestProfileMapper guestProfileMapper;

    @Override
    public GuestProfileDto createGuestProfile(
            GuestProfileDto guestProfileDto
    ) {

        Long guestId = guestProfileDto.getGuestId();

        Guest guest = findGuestById(guestId);

        if (guestProfileRepository.existsByGuest_Id(guestId)) {
            throw new IllegalArgumentException(
                    "A profile already exists for guest id: " +
                            guestId
            );
        }

        GuestProfile guestProfile =
                guestProfileMapper.toEntity(
                        guestProfileDto,
                        guest
                );

        guestProfile.setId(null);

        GuestProfile savedProfile =
                guestProfileRepository.save(guestProfile);

        return guestProfileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuestProfileDto> getAllGuestProfiles() {

        return guestProfileRepository.findAll()
                .stream()
                .map(guestProfileMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GuestProfileDto getGuestProfileById(
            Long profileId
    ) {

        GuestProfile profile =
                findGuestProfileById(profileId);

        return guestProfileMapper.toDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public GuestProfileDto getGuestProfileByGuestId(
            Long guestId
    ) {

        GuestProfile profile =
                guestProfileRepository.findByGuest_Id(guestId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Guest profile not found for guest id: "
                                                + guestId
                                )
                        );

        return guestProfileMapper.toDto(profile);
    }

    @Override
    public GuestProfileDto updateGuestProfile(
            Long profileId,
            GuestProfileDto guestProfileDto
    ) {

        GuestProfile existingProfile =
                findGuestProfileById(profileId);

        Guest guest =
                findGuestById(guestProfileDto.getGuestId());

        guestProfileRepository
                .findByGuest_Id(guest.getId())
                .filter(profile ->
                        !profile.getId().equals(profileId)
                )
                .ifPresent(profile -> {
                    throw new IllegalArgumentException(
                            "A profile already exists for guest id: "
                                    + guest.getId()
                    );
                });

        guestProfileMapper.updateEntity(
                existingProfile,
                guestProfileDto,
                guest
        );

        GuestProfile updatedProfile =
                guestProfileRepository.save(existingProfile);

        return guestProfileMapper.toDto(updatedProfile);
    }

    @Override
    public void deleteGuestProfile(Long profileId) {

        GuestProfile profile =
                findGuestProfileById(profileId);

        guestProfileRepository.delete(profile);
    }

    private GuestProfile findGuestProfileById(
            Long profileId
    ) {

        return guestProfileRepository.findById(profileId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest profile not found with id: "
                                        + profileId
                        )
                );
    }

    private Guest findGuestById(Long guestId) {

        return guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: " +
                                        guestId
                        )
                );
    }
}