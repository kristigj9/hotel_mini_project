package com.lhind.hotel.service;

import com.lhind.hotel.dto.ReservationRequestDto;
import com.lhind.hotel.dto.ReservationResponseDto;
import com.lhind.hotel.entity.Guest;
import com.lhind.hotel.entity.Reservation;
import com.lhind.hotel.entity.ReservationStatus;
import com.lhind.hotel.entity.Room;
import com.lhind.hotel.exception.ResourceNotFoundException;
import com.lhind.hotel.exception.RoomCapacityExceededException;
import com.lhind.hotel.exception.RoomNotAvailableException;
import com.lhind.hotel.mapper.ReservationMapper;
import com.lhind.hotel.repo.GuestRepo;
import com.lhind.hotel.repo.ReservationRepo;
import com.lhind.hotel.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl
        implements ReservationService {

    private final ReservationRepo reservationRepository;
    private final GuestRepo guestRepository;
    private final RoomRepo roomRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDto createReservation(
            ReservationRequestDto reservationDto
    ) {

        validateDates(
                reservationDto.getCheckInDate(),
                reservationDto.getCheckOutDate()
        );

        Guest guest =
                findGuestById(reservationDto.getGuestId());

        Room room =
                findRoomById(reservationDto.getRoomId());

        validateRoomCapacity(
                room,
                reservationDto.getNumberOfGuests()
        );

        validateRoomAvailability(
                room.getId(),
                reservationDto.getCheckInDate(),
                reservationDto.getCheckOutDate(),
                null
        );

        Reservation reservation =
                reservationMapper.toEntity(
                        reservationDto,
                        guest,
                        room
                );

        reservation.setId(null);

        reservation.setTotalPrice(
                calculateTotalPrice(
                        room,
                        reservationDto.getCheckInDate(),
                        reservationDto.getCheckOutDate()
                )
        );

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );

        reservation.setCreatedAt(
                LocalDateTime.now()
        );

        Reservation savedReservation =
                reservationRepository.save(reservation);

        return reservationMapper
                .toResponseDto(savedReservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDto>
    getAllReservations() {

        return reservationRepository.findAll()
                .stream()
                .map(reservationMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDto getReservationById(
            Long reservationId
    ) {

        Reservation reservation =
                findReservationById(reservationId);

        return reservationMapper
                .toResponseDto(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDto>
    getReservationsByGuestId(Long guestId) {

        if (!guestRepository.existsById(guestId)) {
            throw new ResourceNotFoundException(
                    "Guest not found with id: " + guestId
            );
        }

        return reservationRepository
                .findByGuest_Id(guestId)
                .stream()
                .map(reservationMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDto>
    getReservationsByRoomId(Long roomId) {

        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException(
                    "Room not found with id: " + roomId
            );
        }

        return reservationRepository
                .findByRoom_Id(roomId)
                .stream()
                .map(reservationMapper::toResponseDto)
                .toList();
    }

    @Override
    public ReservationResponseDto updateReservation(
            Long reservationId,
            ReservationRequestDto reservationDto
    ) {

        Reservation existingReservation =
                findReservationById(reservationId);

        validateDates(
                reservationDto.getCheckInDate(),
                reservationDto.getCheckOutDate()
        );

        Guest guest =
                findGuestById(reservationDto.getGuestId());

        Room room =
                findRoomById(reservationDto.getRoomId());

        validateRoomCapacity(
                room,
                reservationDto.getNumberOfGuests()
        );

        validateRoomAvailability(
                room.getId(),
                reservationDto.getCheckInDate(),
                reservationDto.getCheckOutDate(),
                reservationId
        );

        reservationMapper.updateEntity(
                existingReservation,
                reservationDto,
                guest,
                room
        );

        existingReservation.setTotalPrice(
                calculateTotalPrice(
                        room,
                        reservationDto.getCheckInDate(),
                        reservationDto.getCheckOutDate()
                )
        );

        Reservation updatedReservation =
                reservationRepository.save(
                        existingReservation
                );

        return reservationMapper
                .toResponseDto(updatedReservation);
    }

    @Override
    public ReservationResponseDto cancelReservation(
            Long reservationId
    ) {

        Reservation reservation =
                findReservationById(reservationId);

        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Reservation is already cancelled"
            );
        }

        if (reservation.getStatus()
                == ReservationStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "A completed reservation cannot be cancelled"
            );
        }

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );

        Reservation cancelledReservation =
                reservationRepository.save(reservation);

        return reservationMapper
                .toResponseDto(cancelledReservation);
    }

    @Override
    public void deleteReservation(Long reservationId) {

        Reservation reservation =
                findReservationById(reservationId);

        reservationRepository.delete(reservation);
    }

    private void validateDates(
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {

        if (checkInDate == null ||
                checkOutDate == null) {

            throw new IllegalArgumentException(
                    "Check-in and check-out dates are required"
            );
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after check-in date"
            );
        }
    }

    private void validateRoomCapacity(
            Room room,
            Integer numberOfGuests
    ) {

        if (numberOfGuests == null ||
                numberOfGuests <= 0) {

            throw new IllegalArgumentException(
                    "Number of guests must be greater than zero"
            );
        }

        if (numberOfGuests > room.getCapacity()) {
            throw new RoomCapacityExceededException(
                    "Room capacity is " +
                            room.getCapacity() +
                            ", but the reservation is for " +
                            numberOfGuests +
                            " guests"
            );
        }
    }

    private void validateRoomAvailability(
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Long excludedReservationId
    ) {

        long overlappingReservations;

        if (excludedReservationId == null) {

            overlappingReservations =
                    reservationRepository
                            .countOverlappingReservations(
                                    roomId,
                                    checkInDate,
                                    checkOutDate
                            );

        } else {

            overlappingReservations =
                    reservationRepository
                            .countOverlappingReservationsExceptCurrent(
                                    roomId,
                                    checkInDate,
                                    checkOutDate,
                                    excludedReservationId
                            );
        }

        if (overlappingReservations > 0) {
            throw new RoomNotAvailableException(
                    "Room is not available for the selected dates"
            );
        }
    }

    private BigDecimal calculateTotalPrice(
            Room room,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {

        long numberOfNights =
                ChronoUnit.DAYS.between(
                        checkInDate,
                        checkOutDate
                );

        return room.getPricePerNight()
                .multiply(
                        BigDecimal.valueOf(numberOfNights)
                );
    }

    private Reservation findReservationById(
            Long reservationId
    ) {

        return reservationRepository
                .findById(reservationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found with id: "
                                        + reservationId
                        )
                );
    }

    private Guest findGuestById(Long guestId) {

        return guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Guest not found with id: "
                                        + guestId
                        )
                );
    }

    private Room findRoomById(Long roomId) {

        return roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: "
                                        + roomId
                        )
                );
    }
}