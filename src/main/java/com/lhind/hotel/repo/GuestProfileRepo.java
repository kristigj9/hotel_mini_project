package com.lhind.hotel.repo;


import com.lhind.hotel.entity.GuestProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestProfileRepo
        extends JpaRepository<GuestProfile, Long> {

    // Gjen profilin nëpërmjet ID-së së guest-it.
    Optional<GuestProfile> findByGuestId(Long guestId);

    // Kontrollon nëse guest-i ka tashmë një profil.
    boolean existsByGuestId(Long guestId);

    // Fshin profilin sipas ID-së së guest-it.
    void deleteByGuestId(Long guestId);
}
