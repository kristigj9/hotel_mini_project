package com.lhind.hotel.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log =
            LogManager.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {

        log.trace(
                "Entering generateToken() for username={}",
                userDetails.getUsername()
        );

        log.debug(
                "Preparing JWT timestamps for username={}",
                userDetails.getUsername()
        );

        Date issuedAt = new Date();

        Date expirationDate =
                new Date(
                        issuedAt.getTime() + jwtExpiration
                );

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();

        log.info(
                "JWT generated successfully for username={}, expiration={}",
                userDetails.getUsername(),
                expirationDate
        );

        return token;
    }

    public String extractUsername(String token) {

        log.trace("Entering extractUsername()");

        String username =
                extractAllClaims(token).getSubject();

        log.debug(
                "Username extracted successfully from JWT: {}",
                username
        );

        return username;
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        log.trace(
                "Entering isTokenValid() for username={}",
                userDetails.getUsername()
        );

        try {
            String username =
                    extractUsername(token);

            boolean usernameMatches =
                    username.equals(
                            userDetails.getUsername()
                    );

            boolean expired =
                    isTokenExpired(token);

            boolean valid =
                    usernameMatches && !expired;

            if (valid) {
                log.info(
                        "JWT is valid for username={}",
                        userDetails.getUsername()
                );
            } else {
                log.warn(
                        "JWT validation failed. usernameMatches={}, expired={}",
                        usernameMatches,
                        expired
                );
            }

            return valid;

        } catch (JwtException | IllegalArgumentException exception) {
            log.error(
                    "JWT validation failed for username={}: {}",
                    userDetails.getUsername(),
                    exception.getMessage()
            );

            return false;
        }
    }

    private boolean isTokenExpired(String token) {

        log.debug("Checking JWT expiration");

        Date expiration =
                extractAllClaims(token).getExpiration();

        boolean expired =
                expiration.before(new Date());

        if (expired) {
            log.warn(
                    "JWT has expired at {}",
                    expiration
            );
        }

        return expired;
    }

    private Claims extractAllClaims(String token) {

        log.debug("Parsing JWT claims");

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {

        log.debug("Creating JWT signing key");

        byte[] keyBytes =
                Decoders.BASE64.decode(jwtSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}