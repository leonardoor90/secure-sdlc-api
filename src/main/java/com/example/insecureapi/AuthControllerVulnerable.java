package com.example.insecureapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// !!! VULNERABILIDADE INTENCIONAL !!!
// Este código contém vulnerabilidades de segurança para demonstração

@RestController
@RequestMapping("/api/vulnerable")
public class AuthControllerVulnerable {

    // VULNERABILIDADE 1: Hardcoded Secret
    private static final String JWT_SECRET = "12345678901234567890123456789012";
    
    // VULNERABILIDADE 2: SQL Injection
    @GetMapping("/users")
    public List<User> searchUsers(@RequestParam String name) {
        String query = "SELECT * FROM users WHERE name LIKE '%" + name + "%'";
        return jdbcTemplate.query(query, new UserRowMapper());
    }
    
    // VULNERABILIDADE 3: Mass Assignment
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userRepository.save(user); // Permite atualizar role
    }
    
    // VULNERABILIDADE 4: Logging de dados sensíveis
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        String token = generateToken(request);
        log.info("User logged in with token: " + token); // Token em log!
        return token;
    }
    
    // VULNERABILIDADE 5: Endpoint sem autenticação
    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return userService.findAll(); // Sem verificação de role!
    }
}
