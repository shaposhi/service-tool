package com.example.servicetools.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_notifications")
public class LogNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 50)
    private String level;

    @Column(length = 100)
    private String source;

    public LogNotification(String message, String level, String source) {
        this.message = message;
        this.level = level;
        this.source = source;
        this.timestamp = LocalDateTime.now();
    }
}
