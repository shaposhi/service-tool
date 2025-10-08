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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogNotificationService {

    @Autowired
    private LogNotificationRepository repository;

    // Create operations
    public LogNotification createLogNotification(LogNotification notification) {
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

    /* public List<LogNotification> getLogNotificationsByCMode(String cMode) {
        return repository.findByCMode(cMode);
    } */

   /*  public Page<LogNotification> getLogNotificationsByCMode(String cMode, Pageable pageable) {
        return repository.findByCMode(cMode, pageable);
    } */


    /* public Page<LogNotification> getLogNotificationsByCMode(String cMode, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByCMode(cMode, pageable);
    } */

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

    public List<LogNotification> getLogNotificationsByPartyId(Long partyId) {
        return repository.findByPartyId(partyId);
    }

    public Page<LogNotification> getLogNotificationsByPartyId(Long partyId, Pageable pageable) {
        return repository.findByPartyId(partyId, pageable);
    }

    public Page<LogNotification> getLogNotificationsByPartyId(Long partyId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByPartyId(partyId, pageable);
    }

    public List<LogNotification> getLogNotificationsBySuccess(Boolean succesfullyProcessed) {
        return repository.findBySuccesfullyProcessed(succesfullyProcessed);
    }

    public Page<LogNotification> getLogNotificationsBySuccess(Boolean succesfullyProcessed, Pageable pageable) {
        return repository.findBySuccesfullyProcessed(succesfullyProcessed, pageable);
    }

    public Page<LogNotification> getLogNotificationsBySuccess(Boolean succesfullyProcessed, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findBySuccesfullyProcessed(succesfullyProcessed, pageable);
    }

    public List<LogNotification> getByLastUpdateRange(ZonedDateTime start, ZonedDateTime end) {
        return repository.findByLastUpdateTimeBetween(start, end);
    }

    public Page<LogNotification> getByLastUpdateRange(ZonedDateTime start, ZonedDateTime end, Pageable pageable) {
        return repository.findByLastUpdateTimeBetween(start, end, pageable);
    }

    public Page<LogNotification> getByLastUpdateRange(ZonedDateTime start, ZonedDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByLastUpdateTimeBetween(start, end, pageable);
    }

    public List<LogNotification> getByReceivedRange(ZonedDateTime start, ZonedDateTime end) {
        return repository.findByReceivedTimeBetween(start, end);
    }

    public Page<LogNotification> getByReceivedRange(ZonedDateTime start, ZonedDateTime end, Pageable pageable) {
        return repository.findByReceivedTimeBetween(start, end, pageable);
    }

    public Page<LogNotification> getByReceivedRange(ZonedDateTime start, ZonedDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByReceivedTimeBetween(start, end, pageable);
    }

    public List<LogNotification> getByCompletedRange(ZonedDateTime start, ZonedDateTime end) {
        return repository.findByCompletedTimeBetween(start, end);
    }

    public Page<LogNotification> getByCompletedRange(ZonedDateTime start, ZonedDateTime end, Pageable pageable) {
        return repository.findByCompletedTimeBetween(start, end, pageable);
    }

    public Page<LogNotification> getByCompletedRange(ZonedDateTime start, ZonedDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByCompletedTimeBetween(start, end, pageable);
    }

    public List<LogNotification> getRecentSince(ZonedDateTime since) {
        return repository.findRecentSince(since);
    }

    public Page<LogNotification> getRecentSince(ZonedDateTime since, Pageable pageable) {
        return repository.findRecentSince(since, pageable);
    }

    public Page<LogNotification> getRecentSince(ZonedDateTime since, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findRecentSince(since, pageable);
    }

    // Update operations
    public LogNotification updateLogNotification(Long id, LogNotification updatedNotification) {
        Optional<LogNotification> existingNotification = repository.findById(id);
        if (existingNotification.isPresent()) {
            LogNotification notification = existingNotification.get();
            notification.setPartyId(updatedNotification.getPartyId());
            notification.setCMode(updatedNotification.getCMode());
            notification.setLastUpdateTime(updatedNotification.getLastUpdateTime());
            notification.setSource(updatedNotification.getSource());
            notification.setReceivedTime(updatedNotification.getReceivedTime());
            notification.setCompletedTime(updatedNotification.getCompletedTime());
            notification.setSuccesfullyProcessed(updatedNotification.getSuccesfullyProcessed());
            notification.setStackTrace(updatedNotification.getStackTrace());
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

    public void deleteOldLogNotifications(ZonedDateTime cutoffTime) {
        repository.deleteByReceivedTimeBefore(cutoffTime);
    }

    public void deleteOldLogNotificationsByDays(int days) {
        ZonedDateTime cutoffTime = ZonedDateTime.now().minusDays(days);
        repository.deleteByReceivedTimeBefore(cutoffTime);
    }

    // Utility operations
    public long getLogNotificationCount() {
        return repository.count();
    }

    public boolean existsLogNotification(Long id) {
        return repository.existsById(id);
    }

    /* public List<Object[]> getLogNotificationCountByCMode() {
        return repository.countByCMode();
    } */

    // Bulk operations
    public List<LogNotification> createMultipleLogNotifications(List<LogNotification> notifications) {
        return repository.saveAll(notifications);
    }

    // Search with multiple parameters
    public List<LogNotification> searchByMultipleParams(Long partyId, Boolean success, String source, String cMode,
                                                       ZonedDateTime start, ZonedDateTime end) {
        return repository.findByMultipleParams(partyId, success, source, cMode, start, end);
    }

    public Page<LogNotification> searchByMultipleParams(Long partyId, Boolean success, String source, String cMode,
                                                       ZonedDateTime start, ZonedDateTime end, Pageable pageable) {
        return repository.findByMultipleParams(partyId, success, source, cMode, start, end, pageable);
    }

    public Page<LogNotification> searchByMultipleParams(Long partyId, Boolean success, String source, String cMode,
                                                       ZonedDateTime start, ZonedDateTime end, 
                                                       int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByMultipleParams(partyId, success, source, cMode, start, end, pageable);
    }
}