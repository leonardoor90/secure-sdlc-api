package com.example.secureapi.controller;

import com.example.secureapi.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(Authentication authentication) {
        return ResponseEntity.ok("Perfil do usuário: " + authentication.getName());
    }
}
