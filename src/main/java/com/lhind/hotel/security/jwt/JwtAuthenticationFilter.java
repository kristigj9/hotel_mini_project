package com.lhind.hotel.security.jwt;

import com.lhind.hotel.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private static final Logger log =
            LogManager.getLogger(
                    JwtAuthenticationFilter.class
            );

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        log.trace(
                "Entering JWT filter. method={}, path={}",
                request.getMethod(),
                request.getRequestURI()
        );

        String authorizationHeader =
                request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            log.debug(
                    "No Bearer token found. path={}",
                    request.getRequestURI()
            );

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authorizationHeader.substring(7);

        try {
            String email =
                    jwtService.extractUsername(token);

            log.debug(
                    "JWT username extracted. email={}, path={}",
                    email,
                    request.getRequestURI()
            );

            boolean authenticationMissing =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null;

            if (email != null && authenticationMissing) {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                if (jwtService.isTokenValid(
                        token,
                        userDetails
                )) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    log.info(
                            "JWT authentication completed successfully. email={}, path={}",
                            email,
                            request.getRequestURI()
                    );

                } else {
                    log.warn(
                            "Invalid JWT received. email={}, path={}",
                            email,
                            request.getRequestURI()
                    );
                }

            } else if (!authenticationMissing) {
                log.debug(
                        "SecurityContext already contains authentication. path={}",
                        request.getRequestURI()
                );
            }

        } catch (Exception exception) {

            SecurityContextHolder.clearContext();

            log.error(
                    "JWT authentication failed. path={}, exception={}",
                    request.getRequestURI(),
                    exception.getClass().getSimpleName()
            );

            log.debug(
                    "JWT authentication exception details",
                    exception
            );
        }

        filterChain.doFilter(request, response);
    }
}