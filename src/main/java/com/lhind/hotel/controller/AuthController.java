package com.lhind.hotel.controller;

import com.lhind.hotel.dto.request.LoginRequest;
import com.lhind.hotel.dto.request.RegisterRequest;
import com.lhind.hotel.dto.response.TokenResponse;
import com.lhind.hotel.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "User registration and JWT authentication"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Register a user",
            description = """
                    Creates a new user with the GUEST role.
                    Logs TRACE on entry, DEBUG during validation,
                    INFO on success and ERROR when the email already exists.
                    """
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(
            summary = "Login",
            description = """
                    Verifies the email and password and generates a JWT.
                    Logs TRACE on entry, DEBUG during authentication,
                    INFO on success and ERROR when authentication fails.
                    """
    )
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        TokenResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }
}