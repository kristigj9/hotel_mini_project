package com.lhind.hotel.service;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.GuestProfile;
import com.lhind.hotel.repo.GuestProfileRepo;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.service.GuestProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestProfileServiceImpl implements GuestProfileService {

    private final GuestProfileRepo guestProfileRepo;
    private final GuestRepo guestRepo;

    public GuestProfileServiceImpl(
            GuestProfileRepo guestProfileRepo,
            GuestRepo guestRepo
    ) {
        this.guestProfileRepo = guestProfileRepo;
        this.guestRepo = guestRepo;
    }

    @Override
    public GuestProfile createGuestProfile(
            Long guestId,
            GuestProfile guestProfile
    ) {
        Optional<Guest> optionalGuest = guestRepo.findById(guestId);

        if (optionalGuest.isEmpty()) {
            return null;
        }

        Guest guest = optionalGuest.get();

        guestProfile.setGuest(guest);
        guest.setProfile(guestProfile);

        return guestProfileRepo.save(guestProfile);
    }

    @Override
    public List<GuestProfile> getAllGuestProfiles() {
        return guestProfileRepo.findAll();
    }

    @Override
    public Optional<GuestProfile> getGuestProfileById(Long profileId) {
        return guestProfileRepo.findById(profileId);
    }

    @Override
    public Optional<GuestProfile> getGuestProfileByGuestId(Long guestId) {
        return guestProfileRepo.findByGuestId(guestId);
    }

    @Override
    public GuestProfile updateGuestProfile(
            Long profileId,
            GuestProfile guestProfile
    ) {
        Optional<GuestProfile> optionalProfile =
                guestProfileRepo.findById(profileId);

        if (optionalProfile.isEmpty()) {
            return null;
        }

        GuestProfile existingProfile = optionalProfile.get();

        existingProfile.setAddress(guestProfile.getAddress());
        existingProfile.setDateOfBirth(guestProfile.getDateOfBirth());
        existingProfile.setNationality(guestProfile.getNationality());
        existingProfile.setPreferredLanguage(
                guestProfile.getPreferredLanguage()
        );

        return guestProfileRepo.save(existingProfile);
    }

    @Override
    public boolean deleteGuestProfile(Long profileId) {
        Optional<GuestProfile> optionalProfile =
                guestProfileRepo.findById(profileId);

        if (optionalProfile.isEmpty()) {
            return false;
        }

        GuestProfile profile = optionalProfile.get();
        Guest guest = profile.getGuest();

        if (guest != null) {
            guest.setProfile(null);
        }

        guestProfileRepo.delete(profile);
        return true;
    }
}