package com.lhind.hotel.repo;
import com.lhind.hotel.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepo extends JpaRepository<Guest, Long> {

    // Gjen një guest sipas email-it.
    Optional<Guest> findByEmail(String email);

    // Kontrollon nëse email-i ekziston.
    boolean existsByEmail(String email);

    // Kontrollon email-in pa dalluar shkronjat e mëdha
    // nga shkronjat e vogla.
    boolean existsByEmailIgnoreCase(String email);
}