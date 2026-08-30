package com.example.secureapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class RateLimitingConfig {

    // Implementação simples de rate limiting em memória
    @Bean
    public ConcurrentHashMap<String, AtomicInteger> requestCounts() {
        return new ConcurrentHashMap<>();
    }
}
