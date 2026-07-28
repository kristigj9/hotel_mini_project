package com.lhind.hotel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is not valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @OneToOne(
            mappedBy = "guest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private GuestProfile profile;

    @JsonIgnore
    @OneToMany(
            mappedBy = "guest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservation> reservations = new ArrayList<>();

    public Guest() {
    }

    public Guest(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public GuestProfile getProfile() {
        return profile;
    }

    public void setProfile(GuestProfile profile) {
        this.profile = profile;

        if (profile != null && profile.getGuest() != this) {
            profile.setGuest(this);
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations =
                reservations != null ? reservations : new ArrayList<>();

        for (Reservation reservation : this.reservations) {
            reservation.setGuest(this);
        }
    }

    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.add(reservation);
            reservation.setGuest(this);
        }
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null) {
            reservations.remove(reservation);
            reservation.setGuest(null);
        }
    }
}