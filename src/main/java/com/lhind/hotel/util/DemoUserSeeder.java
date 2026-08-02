package com.lhind.hotel.util;

import com.lhind.hotel.entity.AppUser;
import com.lhind.hotel.entity.Role;
import com.lhind.hotel.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoUserSeeder implements CommandLineRunner {

    private static final Logger log =
            LogManager.getLogger(DemoUserSeeder.class);

    private final AppUserRepo appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        log.trace("Entering DemoUserSeeder.run()");

        log.info("Starting demo user seeding process");

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

        log.info("Demo user seeding completed");
    }

    private void createUser(
            String firstName,
            String email,
            String password,
            Role role
    ) {

        log.trace(
                "Entering createUser() for email={}",
                email
        );

        log.debug(
                "Checking whether demo user already exists. email={}",
                email
        );

        if (appUserRepository.findByEmail(email).isPresent()) {

            log.warn(
                    "Demo user already exists. Skipping creation. email={}",
                    email
            );

            return;
        }

        log.debug(
                "Encoding password and preparing demo user. email={}, role={}",
                email,
                role
        );

        AppUser user = new AppUser();

        user.setFirstName(firstName);
        user.setEmail(email);
        user.setPassword(
                passwordEncoder.encode(password)
        );
        user.setRole(role);

        AppUser savedUser =
                appUserRepository.save(user);

        log.info(
                "Demo user created successfully. id={}, email={}, role={}",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}