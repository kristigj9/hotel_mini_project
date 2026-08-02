package com.lhind.hotel.service;

import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.repo.GuestRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private static final Logger log =
            LogManager.getLogger(GuestServiceImpl.class);

    private final GuestRepo guestRepo;

    @Override
    public Guest createGuest(Guest guest) {

        log.trace(
                "Entering createGuest() with email={}",
                guest.getEmail()
        );

        log.debug(
                "Checking whether guest email={} already exists",
                guest.getEmail()
        );

        Optional<Guest> existingGuest =
                guestRepo.findByEmail(guest.getEmail());

        if (existingGuest.isPresent()) {
            log.error(
                    "Guest creation failed because email={} already exists",
                    guest.getEmail()
            );

            return null;
        }

        if (guest.getProfile() != null) {
            log.debug(
                    "Associating profile with guest email={}",
                    guest.getEmail()
            );

            guest.getProfile().setGuest(guest);
        }

        if (guest.getReservations() != null) {
            log.debug(
                    "Associating {} reservations with guest email={}",
                    guest.getReservations().size(),
                    guest.getEmail()
            );

            guest.getReservations().forEach(
                    reservation -> reservation.setGuest(guest)
            );
        }

        Guest savedGuest =
                guestRepo.save(guest);

        log.info(
                "Guest created successfully. id={}, email={}",
                savedGuest.getId(),
                savedGuest.getEmail()
        );

        return savedGuest;
    }

    @Override
    public List<Guest> getAllGuests() {

        log.trace("Entering getAllGuests()");
        log.debug("Retrieving all guests from database");

        List<Guest> guests =
                guestRepo.findAll();

        log.info(
                "Retrieved {} guests",
                guests.size()
        );

        return guests;
    }

    @Override
    public Optional<Guest> getGuestById(Long guestId) {

        log.trace(
                "Entering getGuestById() with id={}",
                guestId
        );

        log.debug(
                "Searching guest with id={}",
                guestId
        );

        Optional<Guest> guest =
                guestRepo.findById(guestId);

        if (guest.isEmpty()) {
            log.warn(
                    "Guest with id={} was not found",
                    guestId
            );
        } else {
            log.info(
                    "Guest with id={} found successfully",
                    guestId
            );
        }

        return guest;
    }

    @Override
    public Optional<Guest> getGuestByEmail(String email) {

        log.trace(
                "Entering getGuestByEmail() with email={}",
                email
        );

        log.debug(
                "Searching guest with email={}",
                email
        );

        Optional<Guest> guest =
                guestRepo.findByEmail(email);

        if (guest.isEmpty()) {
            log.warn(
                    "Guest with email={} was not found",
                    email
            );
        } else {
            log.info(
                    "Guest with email={} found successfully",
                    email
            );
        }

        return guest;
    }

    @Override
    public Guest updateGuest(
            Long guestId,
            Guest guest
    ) {
        log.trace(
                "Entering updateGuest() with id={}",
                guestId
        );

        log.debug(
                "Validating guest update for id={}",
                guestId
        );

        Optional<Guest> optionalGuest =
                guestRepo.findById(guestId);

        if (optionalGuest.isEmpty()) {
            log.warn(
                    "Guest update failed because id={} was not found",
                    guestId
            );

            return null;
        }

        Optional<Guest> guestWithSameEmail =
                guestRepo.findByEmail(guest.getEmail());

        if (guestWithSameEmail.isPresent()
                && !guestWithSameEmail
                .get()
                .getId()
                .equals(guestId)) {

            log.error(
                    "Guest update failed because email={} belongs to another guest",
                    guest.getEmail()
            );

            return null;
        }

        Guest existingGuest =
                optionalGuest.get();

        existingGuest.setFirstName(guest.getFirstName());
        existingGuest.setLastName(guest.getLastName());
        existingGuest.setEmail(guest.getEmail());
        existingGuest.setPhoneNumber(guest.getPhoneNumber());

        Guest updatedGuest =
                guestRepo.save(existingGuest);

        log.info(
                "Guest updated successfully. id={}",
                guestId
        );

        return updatedGuest;
    }

    @Override
    public boolean deleteGuest(Long guestId) {

        log.trace(
                "Entering deleteGuest() with id={}",
                guestId
        );

        log.debug(
                "Checking whether guest id={} exists",
                guestId
        );

        if (!guestRepo.existsById(guestId)) {
            log.error(
                    "Guest deletion failed because id={} does not exist",
                    guestId
            );

            return false;
        }

        guestRepo.deleteById(guestId);

        log.info(
                "Guest deleted successfully. id={}",
                guestId
        );

        return true;
    }
}