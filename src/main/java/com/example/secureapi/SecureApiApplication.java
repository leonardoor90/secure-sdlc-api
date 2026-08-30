package com.example.secureapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.secureapi"})
@EnableJpaRepositories(basePackages = "com.example.secureapi.repository")
@EntityScan(basePackages = "com.example.secureapi.model")
public class SecureApiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SecureApiApplication.class, args);
    }
}
