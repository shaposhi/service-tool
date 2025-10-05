package com.example.servicetools.service;

import com.example.servicetools.dao.LogNotificationRepository;
import com.example.servicetools.model.LogNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogNotificationService {

    @Autowired
    private LogNotificationRepository repository;

    // Create operations
    public LogNotification createLogNotification(String message, String level, String source) {
        LogNotification notification = new LogNotification(message, level, source);
        return repository.save(notification);
    }

    public LogNotification saveLogNotification(LogNotification notification) {
        return repository.save(notification);
    }

    // Read operations
    public List<LogNotification> getAllLogNotifications() {
        return repository.findAll();
    }

    public Page<LogNotification> getAllLogNotifications(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<LogNotification> getAllLogNotifications(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Optional<LogNotification> getLogNotificationById(Long id) {
        return repository.findById(id);
    }

    public List<LogNotification> getLogNotificationsByLevel(String level) {
        return repository.findByLevel(level);
    }

    public Page<LogNotification> getLogNotificationsByLevel(String level, Pageable pageable) {
        return repository.findByLevel(level, pageable);
    }

    public Page<LogNotification> getLogNotificationsByLevel(String level, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByLevel(level, pageable);
    }

    public List<LogNotification> getLogNotificationsBySource(String source) {
        return repository.findBySource(source);
    }

    public Page<LogNotification> getLogNotificationsBySource(String source, Pageable pageable) {
        return repository.findBySource(source, pageable);
    }

    public Page<LogNotification> getLogNotificationsBySource(String source, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findBySource(source, pageable);
    }

    public List<LogNotification> getLogNotificationsByLevelAndSource(String level, String source) {
        return repository.findByLevelAndSource(level, source);
    }

    public Page<LogNotification> getLogNotificationsByLevelAndSource(String level, String source, Pageable pageable) {
        return repository.findByLevelAndSource(level, source, pageable);
    }

    public Page<LogNotification> getLogNotificationsByLevelAndSource(String level, String source, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByLevelAndSource(level, source, pageable);
    }

    public List<LogNotification> getLogNotificationsByTimestampRange(LocalDateTime startTime, LocalDateTime endTime) {
        return repository.findByTimestampBetween(startTime, endTime);
    }

    public Page<LogNotification> getLogNotificationsByTimestampRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        return repository.findByTimestampBetween(startTime, endTime, pageable);
    }

    public Page<LogNotification> getLogNotificationsByTimestampRange(LocalDateTime startTime, LocalDateTime endTime, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByTimestampBetween(startTime, endTime, pageable);
    }

    public List<LogNotification> searchLogNotificationsByMessage(String message) {
        return repository.findByMessageContainingIgnoreCase(message);
    }

    public Page<LogNotification> searchLogNotificationsByMessage(String message, Pageable pageable) {
        return repository.findByMessageContainingIgnoreCase(message, pageable);
    }

    public Page<LogNotification> searchLogNotificationsByMessage(String message, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByMessageContainingIgnoreCase(message, pageable);
    }

    public List<LogNotification> getRecentLogNotifications(LocalDateTime since) {
        return repository.findRecentNotifications(since);
    }

    public Page<LogNotification> getRecentLogNotifications(LocalDateTime since, Pageable pageable) {
        return repository.findRecentNotifications(since, pageable);
    }

    public Page<LogNotification> getRecentLogNotifications(LocalDateTime since, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findRecentNotifications(since, pageable);
    }

    public List<LogNotification> getRecentLogNotifications(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return repository.findRecentNotifications(since);
    }

    public Page<LogNotification> getRecentLogNotifications(int hours, Pageable pageable) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return repository.findRecentNotifications(since, pageable);
    }

    public Page<LogNotification> getRecentLogNotifications(int hours, int page, int size, String sortBy, String sortDirection) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findRecentNotifications(since, pageable);
    }

    // Update operations
    public LogNotification updateLogNotification(Long id, LogNotification updatedNotification) {
        Optional<LogNotification> existingNotification = repository.findById(id);
        if (existingNotification.isPresent()) {
            LogNotification notification = existingNotification.get();
            notification.setMessage(updatedNotification.getMessage());
            notification.setLevel(updatedNotification.getLevel());
            notification.setSource(updatedNotification.getSource());
            // Note: timestamp is not updated to preserve original creation time
            return repository.save(notification);
        }
        throw new RuntimeException("LogNotification not found with id: " + id);
    }

    public LogNotification updateLogNotificationMessage(Long id, String newMessage) {
        Optional<LogNotification> existingNotification = repository.findById(id);
        if (existingNotification.isPresent()) {
            LogNotification notification = existingNotification.get();
            notification.setMessage(newMessage);
            return repository.save(notification);
        }
        throw new RuntimeException("LogNotification not found with id: " + id);
    }

    // Delete operations
    public void deleteLogNotification(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("LogNotification not found with id: " + id);
        }
    }

    public void deleteAllLogNotifications() {
        repository.deleteAll();
    }

    public void deleteOldLogNotifications(LocalDateTime cutoffTime) {
        repository.deleteByTimestampBefore(cutoffTime);
    }

    public void deleteOldLogNotifications(int days) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
        repository.deleteByTimestampBefore(cutoffTime);
    }

    // Utility operations
    public long getLogNotificationCount() {
        return repository.count();
    }

    public boolean existsLogNotification(Long id) {
        return repository.existsById(id);
    }

    public List<Object[]> getLogNotificationCountByLevel() {
        return repository.countByLevel();
    }

    // Bulk operations
    public List<LogNotification> createMultipleLogNotifications(List<LogNotification> notifications) {
        return repository.saveAll(notifications);
    }
}