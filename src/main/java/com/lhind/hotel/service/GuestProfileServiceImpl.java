package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.GuestProfile;
import com.lhind.hotel.repo.GuestProfileRepo;
import com.lhind.hotel.repo.GuestRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestProfileServiceImpl
        implements GuestProfileService {

    private static final Logger log =
            LogManager.getLogger(
                    GuestProfileServiceImpl.class
            );

    private final GuestProfileRepo guestProfileRepo;
    private final GuestRepo guestRepo;

    @Override
    public GuestProfile createGuestProfile(
            Long guestId,
            GuestProfile guestProfile
    ) {
        log.trace(
                "Entering createGuestProfile() for guestId={}",
                guestId
        );

        log.debug(
                "Searching guest before creating profile. guestId={}",
                guestId
        );

        Optional<Guest> optionalGuest =
                guestRepo.findById(guestId);

        if (optionalGuest.isEmpty()) {
            log.warn(
                    "Profile creation failed because guestId={} was not found",
                    guestId
            );

            return null;
        }

        Guest guest =
                optionalGuest.get();

        guestProfile.setGuest(guest);
        guest.setProfile(guestProfile);

        GuestProfile savedProfile =
                guestProfileRepo.save(guestProfile);

        log.info(
                "Guest profile created successfully. profileId={}, guestId={}",
                savedProfile.getId(),
                guestId
        );

        return savedProfile;
    }

    @Override
    public List<GuestProfile> getAllGuestProfiles() {

        log.trace("Entering getAllGuestProfiles()");
        log.debug("Retrieving all guest profiles");

        List<GuestProfile> profiles =
                guestProfileRepo.findAll();

        log.info(
                "Retrieved {} guest profiles",
                profiles.size()
        );

        return profiles;
    }

    @Override
    public Optional<GuestProfile> getGuestProfileById(
            Long profileId
    ) {
        log.trace(
                "Entering getGuestProfileById() with id={}",
                profileId
        );

        Optional<GuestProfile> profile =
                guestProfileRepo.findById(profileId);

        if (profile.isEmpty()) {
            log.warn(
                    "Guest profile with id={} was not found",
                    profileId
            );
        } else {
            log.info(
                    "Guest profile with id={} found successfully",
                    profileId
            );
        }

        return profile;
    }

    @Override
    public Optional<GuestProfile> getGuestProfileByGuestId(
            Long guestId
    ) {
        log.trace(
                "Entering getGuestProfileByGuestId() with guestId={}",
                guestId
        );

        Optional<GuestProfile> profile =
                guestProfileRepo.findByGuestId(guestId);

        if (profile.isEmpty()) {
            log.warn(
                    "No profile was found for guestId={}",
                    guestId
            );
        } else {
            log.info(
                    "Profile found successfully for guestId={}",
                    guestId
            );
        }

        return profile;
    }

    @Override
    public GuestProfile updateGuestProfile(
            Long profileId,
            GuestProfile guestProfile
    ) {
        log.trace(
                "Entering updateGuestProfile() with profileId={}",
                profileId
        );

        log.debug(
                "Validating profile update for profileId={}",
                profileId
        );

        Optional<GuestProfile> optionalProfile =
                guestProfileRepo.findById(profileId);

        if (optionalProfile.isEmpty()) {
            log.warn(
                    "Profile update failed because profileId={} was not found",
                    profileId
            );

            return null;
        }

        GuestProfile existingProfile =
                optionalProfile.get();

        existingProfile.setAddress(
                guestProfile.getAddress()
        );
        existingProfile.setDateOfBirth(
                guestProfile.getDateOfBirth()
        );
        existingProfile.setNationality(
                guestProfile.getNationality()
        );
        existingProfile.setPreferredLanguage(
                guestProfile.getPreferredLanguage()
        );

        GuestProfile updatedProfile =
                guestProfileRepo.save(existingProfile);

        log.info(
                "Guest profile updated successfully. profileId={}",
                profileId
        );

        return updatedProfile;
    }

    @Override
    public boolean deleteGuestProfile(Long profileId) {

        log.trace(
                "Entering deleteGuestProfile() with profileId={}",
                profileId
        );

        Optional<GuestProfile> optionalProfile =
                guestProfileRepo.findById(profileId);

        if (optionalProfile.isEmpty()) {
            log.error(
                    "Profile deletion failed because profileId={} does not exist",
                    profileId
            );

            return false;
        }

        GuestProfile profile =
                optionalProfile.get();

        Guest guest =
                profile.getGuest();

        if (guest != null) {
            log.debug(
                    "Removing profile association from guestId={}",
                    guest.getId()
            );

            guest.setProfile(null);
        }

        guestProfileRepo.delete(profile);

        log.info(
                "Guest profile deleted successfully. profileId={}",
                profileId
        );

        return true;
    }
}