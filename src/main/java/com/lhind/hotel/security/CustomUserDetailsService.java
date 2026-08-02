package com.lhind.hotel.security;

import com.lhind.hotel.entity.AppUser;
import com.lhind.hotel.repo.AppUserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log =
            LogManager.getLogger(CustomUserDetailsService.class);

    private final AppUserRepo appUserRepository;

    public CustomUserDetailsService(
            AppUserRepo appUserRepository
    ) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        log.trace(
                "Entering loadUserByUsername() with email={}",
                email
        );

        log.debug(
                "Searching application user with email={}",
                email
        );

        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn(
                            "Authentication user was not found. email={}",
                            email
                    );

                    return new UsernameNotFoundException(
                            "User with email "
                                    + email
                                    + " was not found"
                    );
                });

        String authority =
                "ROLE_" + appUser.getRole().name();

        log.info(
                "User details loaded successfully. email={}, authority={}",
                email,
                authority
        );

        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(authority)
                )
        );
    }
}