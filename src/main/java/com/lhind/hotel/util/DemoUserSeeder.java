package com.lhind.hotel.util;

import com.lhind.hotel.entity.AppUser;
import com.lhind.hotel.entity.Role;
import com.lhind.hotel.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoUserSeeder implements CommandLineRunner {

    private final AppUserRepo appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createUser(
                "Admin",
                "admin@hotel.com",
                "123456",
                Role.ADMIN
        );

        createUser(
                "Reception",
                "reception@hotel.com",
                "123456",
                Role.RECEPTIONIST
        );

        createUser(
                "Guest",
                "guest@hotel.com",
                "123456",
                Role.GUEST
        );
    }

    private void createUser(
            String firstName,
            String email,
            String password,
            Role role
    ) {

        if (appUserRepository.findByEmail(email).isPresent()) {
            return;
        }

        AppUser user = new AppUser();

        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        appUserRepository.save(user);
    }
}