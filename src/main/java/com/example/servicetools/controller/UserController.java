package com.example.servicetools.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        // Stub data
        Map<String, Object> user = new HashMap<>();
        user.put("username", "John Doe");
        user.put("email", "john.doe@example.com");
        user.put("roles", new String[]{"USER", "ADMIN"});
        
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Redirect to local logout page
        return ResponseEntity.status(302)
                .header("Location", "/service-tool/logout.html")
                .build();
    }
}