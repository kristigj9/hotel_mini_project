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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepo appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {

        if (appUserRepository.existsByEmail(request.email())) {
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

        appUserRepository.save(appUser);
    }

    public TokenResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(request.email());

        String token = jwtService.generateToken(userDetails);

        return new TokenResponse(token, "Bearer");
    }
}