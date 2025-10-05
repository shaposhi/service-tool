package com.example.servicetools.controller;

import com.example.servicetools.model.LogNotification;
import com.example.servicetools.service.LogNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/log-notifications")
public class WebController {

    @Autowired
    private LogNotificationService logNotificationService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    // REST API endpoints for LogNotification CRUD operations

    // Create a new log notification
    @PostMapping
    public ResponseEntity<LogNotification> createLogNotification(@RequestBody LogNotificationRequest request) {
        try {
            LogNotification notification = logNotificationService.createLogNotification(
                request.getMessage(), 
                request.getLevel(), 
                request.getSource()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(notification);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all log notifications
    @GetMapping
    public ResponseEntity<List<LogNotification>> getAllLogNotifications() {
        List<LogNotification> notifications = logNotificationService.getAllLogNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Get all log notifications with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<LogNotification>> getAllLogNotificationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getAllLogNotifications(
            page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get log notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<LogNotification> getLogNotificationById(@PathVariable Long id) {
        Optional<LogNotification> notification = logNotificationService.getLogNotificationById(id);
        return notification.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    // Get log notifications by level
    @GetMapping("/level/{level}")
    public ResponseEntity<List<LogNotification>> getLogNotificationsByLevel(@PathVariable String level) {
        List<LogNotification> notifications = logNotificationService.getLogNotificationsByLevel(level);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by level with pagination
    @GetMapping("/level/{level}/paginated")
    public ResponseEntity<Page<LogNotification>> getLogNotificationsByLevelPaginated(
            @PathVariable String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getLogNotificationsByLevel(
            level, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by source
    @GetMapping("/source/{source}")
    public ResponseEntity<List<LogNotification>> getLogNotificationsBySource(@PathVariable String source) {
        List<LogNotification> notifications = logNotificationService.getLogNotificationsBySource(source);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by source with pagination
    @GetMapping("/source/{source}/paginated")
    public ResponseEntity<Page<LogNotification>> getLogNotificationsBySourcePaginated(
            @PathVariable String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getLogNotificationsBySource(
            source, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Search log notifications by message
    @GetMapping("/search")
    public ResponseEntity<List<LogNotification>> searchLogNotifications(@RequestParam String message) {
        List<LogNotification> notifications = logNotificationService.searchLogNotificationsByMessage(message);
        return ResponseEntity.ok(notifications);
    }

    // Search log notifications by message with pagination
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<LogNotification>> searchLogNotificationsPaginated(
            @RequestParam String message,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.searchLogNotificationsByMessage(
            message, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get recent log notifications
    @GetMapping("/recent")
    public ResponseEntity<List<LogNotification>> getRecentLogNotifications(@RequestParam(defaultValue = "24") int hours) {
        List<LogNotification> notifications = logNotificationService.getRecentLogNotifications(hours);
        return ResponseEntity.ok(notifications);
    }

    // Get recent log notifications with pagination
    @GetMapping("/recent/paginated")
    public ResponseEntity<Page<LogNotification>> getRecentLogNotificationsPaginated(
            @RequestParam(defaultValue = "24") int hours,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getRecentLogNotifications(
            hours, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Update log notification
    @PutMapping("/{id}")
    public ResponseEntity<LogNotification> updateLogNotification(@PathVariable Long id, @RequestBody LogNotificationRequest request) {
        try {
            LogNotification updatedNotification = new LogNotification(
                request.getMessage(), 
                request.getLevel(), 
                request.getSource()
            );
            
            LogNotification result = logNotificationService.updateLogNotification(id, updatedNotification);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete log notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogNotification(@PathVariable Long id) {
        try {
            logNotificationService.deleteLogNotification(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete all log notifications
    @DeleteMapping
    public ResponseEntity<Void> deleteAllLogNotifications() {
        logNotificationService.deleteAllLogNotifications();
        return ResponseEntity.noContent().build();
    }

    // Get log notification count
    @GetMapping("/count")
    public ResponseEntity<Long> getLogNotificationCount() {
        long count = logNotificationService.getLogNotificationCount();
        return ResponseEntity.ok(count);
    }

    // Get log notification count by level
    @GetMapping("/count-by-level")
    public ResponseEntity<List<Object[]>> getLogNotificationCountByLevel() {
        List<Object[]> countByLevel = logNotificationService.getLogNotificationCountByLevel();
        return ResponseEntity.ok(countByLevel);
    }

    // Inner class for request body
    public static class LogNotificationRequest {
        private String message;
        private String level;
        private String source;

        // Getters and setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public String getLevel() { return level; }
        public void setLevel(String level) { this.level = level; }
        
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
    }
}

