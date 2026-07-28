package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.service.GuestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepo guestRepo;

    public GuestServiceImpl(GuestRepo guestRepo) {
        this.guestRepo = guestRepo;
    }

    @Override
    public Guest createGuest(Guest guest) {

        Optional<Guest> existingGuest =
                guestRepo.findByEmail(guest.getEmail());

        if (existingGuest.isPresent()) {
            return null;
        }

        if (guest.getProfile() != null) {
            guest.getProfile().setGuest(guest);
        }

        if (guest.getReservations() != null) {
            guest.getReservations().forEach(
                    reservation -> reservation.setGuest(guest)
            );
        }

        return guestRepo.save(guest);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepo.findAll();
    }

    @Override
    public Optional<Guest> getGuestById(Long guestId) {
        return guestRepo.findById(guestId);
    }

    @Override
    public Optional<Guest> getGuestByEmail(String email) {
        return guestRepo.findByEmail(email);
    }

    @Override
    public Guest updateGuest(Long guestId, Guest guest) {

        Optional<Guest> optionalGuest = guestRepo.findById(guestId);

        if (optionalGuest.isEmpty()) {
            return null;
        }

        Guest existingGuest = optionalGuest.get();

        Optional<Guest> guestWithSameEmail =
                guestRepo.findByEmail(guest.getEmail());

        if (guestWithSameEmail.isPresent()
                && !guestWithSameEmail.get().getId().equals(guestId)) {
            return null;
        }

        existingGuest.setFirstName(guest.getFirstName());
        existingGuest.setLastName(guest.getLastName());
        existingGuest.setEmail(guest.getEmail());
        existingGuest.setPhoneNumber(guest.getPhoneNumber());

        return guestRepo.save(existingGuest);
    }

    @Override
    public boolean deleteGuest(Long guestId) {

        Optional<Guest> optionalGuest = guestRepo.findById(guestId);

        if (optionalGuest.isEmpty()) {
            return false;
        }

        guestRepo.delete(optionalGuest.get());
        return true;
    }
}