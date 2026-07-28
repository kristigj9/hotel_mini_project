package com.lhind.hotel.service;
import com.lhind.hotel.entity.GuestProfile;

import java.util.List;
import java.util.Optional;

public interface GuestProfileService {

    GuestProfile createGuestProfile(
            Long guestId,
            GuestProfile guestProfile
    );

    List<GuestProfile> getAllGuestProfiles();

    Optional<GuestProfile> getGuestProfileById(Long profileId);

    Optional<GuestProfile> getGuestProfileByGuestId(Long guestId);

    GuestProfile updateGuestProfile(
            Long profileId,
            GuestProfile guestProfile
    );

    boolean deleteGuestProfile(Long profileId);
}
