package com.lhind.hotel.repo;

import com.lhind.hotel.entity.GuestProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestProfileRepo
        extends JpaRepository<GuestProfile, Long> {

    Optional<GuestProfile> findByGuest_Id(Long guestId);

    boolean existsByGuest_Id(Long guestId);

    void deleteByGuest_Id(Long guestId);
}