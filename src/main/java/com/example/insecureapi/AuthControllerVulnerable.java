package com.example.insecureapi;

// !!! VULNERABILIDADE INTENCIONAL - APENAS PARA DEMONSTRAÇÃO !!!
// Este arquivo NÃO deve existir na branch main

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vulnerable")
public class AuthControllerVulnerable {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerVulnerable.class);

    // VULN 1: Hardcoded secrets
    private static final String GITHUB_TOKEN = "ghp_1234567890abcdefghijklmnopqrstuvwxyz";
    private static final String DB_PASSWORD = "admin123";

    // VULN 2: SQL Injection via concatenação de string (Agora com sintaxe Java correta)
    @GetMapping("/users/search")
    public List<Map<String, Object>> searchUsers(@RequestParam String name) {
        String query = "SELECT * FROM users WHERE name LIKE '%" + name + "%'";
        log.warn("Executando query vulnerável: {}", query);
        return List.of(Map.of("query", query));
    }

    // VULN 3: Mass Assignment
    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> user) {
        user.put("id", id);
        return user;
    }

    // VULN 4: Endpoint sem autorização
    @GetMapping("/admin/users")
    public List<String> getAllUsers() {
        return List.of("admin", "user");
    }

    // VULN 5: Log de dados sensíveis
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String token = "fake-jwt-token-" + request.get("username");
        log.info("User logged in with token: " + token);
        return Map.of("token", token);
    }
}
