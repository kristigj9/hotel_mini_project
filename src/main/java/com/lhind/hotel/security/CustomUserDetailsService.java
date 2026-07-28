package com.lhind.hotel.security;
import com.lhind.hotel.entity.AppUser;
import com.lhind.hotel.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepo appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User with email " + email + " was not found"
                        )
                );

        return new User(
                appUser.getEmail(),
                appUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + appUser.getRole().name()
                        )
                )
        );
    }


}