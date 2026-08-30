package com.example.secureapi.controller;

import com.example.secureapi.model.User;
import com.example.secureapi.model.dto.JwtResponse;
import com.example.secureapi.model.dto.LoginRequest;
import com.example.secureapi.model.dto.RegisterRequest;
import com.example.secureapi.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
        log.info("AuthController inicializado!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Recebendo requisição de login para usuário: {}", request.getUsername());
        JwtResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Recebendo requisição de registro para usuário: {}", request.getUsername());
        User user = authService.register(request);
        return ResponseEntity.status(201).body(user);
    }
}
