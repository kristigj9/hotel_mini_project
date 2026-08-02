package com.lhind.hotel.service;

import com.lhind.hotel.dto.request.LoginRequest;
import com.lhind.hotel.dto.request.RegisterRequest;
import com.lhind.hotel.dto.response.TokenResponse;
import com.lhind.hotel.entity.AppUser;
import com.lhind.hotel.entity.Role;
import com.lhind.hotel.repo.AppUserRepo;
import com.lhind.hotel.security.CustomUserDetailsService;
import com.lhind.hotel.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log =
            LogManager.getLogger(AuthService.class);

    private final AppUserRepo appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        log.trace(
                "Entering register() for email={}",
                request.email()
        );

        log.debug(
                "Validating registration data for email={}",
                request.email()
        );

        if (appUserRepository.existsByEmail(request.email())) {
            log.error(
                    "Registration failed because email={} already exists",
                    request.email()
            );

            throw new IllegalArgumentException(
                    "A user with this email already exists"
            );
        }

        AppUser appUser = AppUser.builder()
                .firstName(request.firstName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.GUEST)
                .build();

        AppUser savedUser =
                appUserRepository.save(appUser);

        log.info(
                "User registered successfully. id={}, email={}, role={}",
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public TokenResponse login(LoginRequest request) {

        log.trace(
                "Entering login() for email={}",
                request.email()
        );

        log.debug(
                "Authenticating credentials for email={}",
                request.email()
        );

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException exception) {
            log.error(
                    "Authentication failed for email={}",
                    request.email()
            );

            throw exception;
        }

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.email()
                );

        log.debug(
                "Generating JWT for email={}",
                request.email()
        );

        String token =
                jwtService.generateToken(userDetails);

        log.info(
                "User authenticated successfully. email={}",
                request.email()
        );

        return new TokenResponse(token, "Bearer");
    }
}