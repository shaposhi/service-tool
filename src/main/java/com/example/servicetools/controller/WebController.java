package com.example.servicetools.controller;

import com.example.servicetools.model.LogNotification;
import com.example.servicetools.service.LogNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<LogNotification> createLogNotification(@RequestBody LogNotification request) {
        try {
            LogNotification saved = logNotificationService.createLogNotification(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
            @RequestParam(defaultValue = "receivedTime") String sortBy,
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
            @RequestParam(defaultValue = "receivedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getLogNotificationsBySource(
            source, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by partyId (non-paginated)
    @GetMapping("/party/{partyId}")
    public ResponseEntity<List<LogNotification>> getLogNotificationsByPartyId(@PathVariable Long partyId) {
        List<LogNotification> notifications = logNotificationService.getLogNotificationsByPartyId(partyId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/party/{partyId}/paginated")
    public ResponseEntity<Page<LogNotification>> getLogNotificationsByPartyIdPaginated(
            @PathVariable Long partyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "receivedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getLogNotificationsByPartyId(
            partyId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by success flag
    @GetMapping("/success/{flag}")
    public ResponseEntity<List<LogNotification>> getLogNotificationsBySuccess(@PathVariable Boolean flag) {
        List<LogNotification> notifications = logNotificationService.getLogNotificationsBySuccess(flag);
        return ResponseEntity.ok(notifications);
    }

    // Get log notifications by success flag with pagination
    @GetMapping("/success/{flag}/paginated")
    public ResponseEntity<Page<LogNotification>> getLogNotificationsBySuccessPaginated(
            @PathVariable Boolean flag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "receivedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<LogNotification> notifications = logNotificationService.getLogNotificationsBySuccess(
            flag, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Get by received time range
    @GetMapping("/received-range")
    public ResponseEntity<List<LogNotification>> getByReceivedRange(
            @RequestParam String start,
            @RequestParam String end) {
        ZonedDateTime startZdt = parseLocalDateTimeToZoned(start);
        ZonedDateTime endZdt = parseLocalDateTimeToZoned(end);
        List<LogNotification> notifications = logNotificationService.getByReceivedRange(startZdt, endZdt);
        return ResponseEntity.ok(notifications);
    }

    // Get by received time range with pagination
    @GetMapping("/received-range/paginated")
    public ResponseEntity<Page<LogNotification>> getByReceivedRangePaginated(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "receivedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        ZonedDateTime startZdt = parseLocalDateTimeToZoned(start);
        ZonedDateTime endZdt = parseLocalDateTimeToZoned(end);
        Page<LogNotification> notifications = logNotificationService.getByReceivedRange(
            startZdt, endZdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    private ZonedDateTime parseLocalDateTimeToZoned(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Expecting input like "2025-10-04T19:52" from datetime-local
        LocalDateTime ldt = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return ldt.atZone(ZoneId.systemDefault());
    }

    // Get recent log notifications since a given time
    @GetMapping("/recent")
    public ResponseEntity<List<LogNotification>> getRecentLogNotifications(@RequestParam ZonedDateTime since) {
        List<LogNotification> notifications = logNotificationService.getRecentSince(since);
        return ResponseEntity.ok(notifications);
    }

    // Get recent log notifications with pagination
    @GetMapping("/recent/paginated")
    public ResponseEntity<Page<LogNotification>> getRecentLogNotificationsPaginated(
            @RequestParam ZonedDateTime since,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "receivedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<LogNotification> notifications = logNotificationService.getRecentSince(
            since, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(notifications);
    }

    // Update log notification
    @PutMapping("/{id}")
    public ResponseEntity<LogNotification> updateLogNotification(@PathVariable Long id, @RequestBody LogNotification request) {
        try {
            LogNotification result = logNotificationService.updateLogNotification(id, request);
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

}

