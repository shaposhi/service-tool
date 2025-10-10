package com.example.servicetools.service;

import com.example.servicetools.dao.JobLogEntryRepository;
import com.example.servicetools.model.JobLogEntry;
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
public class JobLogEntryService {

    @Autowired
    private JobLogEntryRepository repository;

    // Create operations
    public JobLogEntry createJobLogEntry(JobLogEntry jobLogEntry) {
        return repository.save(jobLogEntry);
    }

    public JobLogEntry saveJobLogEntry(JobLogEntry jobLogEntry) {
        return repository.save(jobLogEntry);
    }

    // Read operations
    public List<JobLogEntry> getAllJobLogEntries() {
        return repository.findAll();
    }

    public Page<JobLogEntry> getAllJobLogEntries(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<JobLogEntry> getAllJobLogEntries(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Optional<JobLogEntry> getJobLogEntryById(Long id) {
        return repository.findById(id);
    }

    public List<JobLogEntry> getJobLogEntriesByJobInstanceId(Long jobInstanceId) {
        return repository.findByJobInstanceId(jobInstanceId);
    }

    public Page<JobLogEntry> getJobLogEntriesByJobInstanceId(Long jobInstanceId, Pageable pageable) {
        return repository.findByJobInstanceId(jobInstanceId, pageable);
    }

    public Page<JobLogEntry> getJobLogEntriesByJobInstanceId(Long jobInstanceId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByJobInstanceId(jobInstanceId, pageable);
    }

    public List<JobLogEntry> getJobLogEntriesByJobName(String jobName) {
        return repository.findByJobName(jobName);
    }

    public Page<JobLogEntry> getJobLogEntriesByJobName(String jobName, Pageable pageable) {
        return repository.findByJobName(jobName, pageable);
    }

    public Page<JobLogEntry> getJobLogEntriesByJobName(String jobName, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByJobName(jobName, pageable);
    }

    public List<JobLogEntry> getJobLogEntriesByRecordId(Long recordId) {
        return repository.findByRecordId(recordId);
    }

    public Page<JobLogEntry> getJobLogEntriesByRecordId(Long recordId, Pageable pageable) {
        return repository.findByRecordId(recordId, pageable);
    }

    public Page<JobLogEntry> getJobLogEntriesByRecordId(Long recordId, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByRecordId(recordId, pageable);
    }

    public List<JobLogEntry> getJobLogEntriesByType(String type) {
        return repository.findByType(type);
    }

    public Page<JobLogEntry> getJobLogEntriesByType(String type, Pageable pageable) {
        return repository.findByType(type, pageable);
    }

    public Page<JobLogEntry> getJobLogEntriesByType(String type, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByType(type, pageable);
    }

    public List<JobLogEntry> getJobLogEntriesByStatus(String status) {
        return repository.findByStatus(status);
    }

    public Page<JobLogEntry> getJobLogEntriesByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    public Page<JobLogEntry> getJobLogEntriesByStatus(String status, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByStatus(status, pageable);
    }

    public List<JobLogEntry> getByEventTsRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByEventTsBetween(start, end);
    }

    public Page<JobLogEntry> getByEventTsRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return repository.findByEventTsBetween(start, end, pageable);
    }

    public Page<JobLogEntry> getByEventTsRange(LocalDateTime start, LocalDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByEventTsBetween(start, end, pageable);
    }

    public List<JobLogEntry> getRecentSince(LocalDateTime since) {
        return repository.findRecentSince(since);
    }

    public Page<JobLogEntry> getRecentSince(LocalDateTime since, Pageable pageable) {
        return repository.findRecentSince(since, pageable);
    }

    public Page<JobLogEntry> getRecentSince(LocalDateTime since, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findRecentSince(since, pageable);
    }

    // Update operations
    public JobLogEntry updateJobLogEntry(Long id, JobLogEntry updatedJobLogEntry) {
        Optional<JobLogEntry> existingJobLogEntry = repository.findById(id);
        if (existingJobLogEntry.isPresent()) {
            JobLogEntry jobLogEntry = existingJobLogEntry.get();
            jobLogEntry.setJobInstanceId(updatedJobLogEntry.getJobInstanceId());
            jobLogEntry.setJobName(updatedJobLogEntry.getJobName());
            jobLogEntry.setRecordId(updatedJobLogEntry.getRecordId());
            jobLogEntry.setType(updatedJobLogEntry.getType());
            jobLogEntry.setStatus(updatedJobLogEntry.getStatus());
            jobLogEntry.setEventTs(updatedJobLogEntry.getEventTs());
            jobLogEntry.setDescription(updatedJobLogEntry.getDescription());
            jobLogEntry.setPayload(updatedJobLogEntry.getPayload());
            jobLogEntry.setStacktrace(updatedJobLogEntry.getStacktrace());
            return repository.save(jobLogEntry);
        }
        throw new RuntimeException("JobLogEntry not found with id: " + id);
    }

    // Delete operations
    public void deleteJobLogEntry(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("JobLogEntry not found with id: " + id);
        }
    }

    public void deleteAllJobLogEntries() {
        repository.deleteAll();
    }

    public void deleteOldJobLogEntries(LocalDateTime cutoffTime) {
        repository.deleteByEventTsBefore(cutoffTime);
    }

    public void deleteOldJobLogEntriesByDays(int days) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
        repository.deleteByEventTsBefore(cutoffTime);
    }

    // Utility operations
    public long getJobLogEntryCount() {
        return repository.count();
    }

    public boolean existsJobLogEntry(Long id) {
        return repository.existsById(id);
    }

    // Bulk operations
    public List<JobLogEntry> createMultipleJobLogEntries(List<JobLogEntry> jobLogEntries) {
        return repository.saveAll(jobLogEntries);
    }

    // Search with multiple parameters
    public List<JobLogEntry> searchByMultipleParams(Long jobInstanceId, String jobName, Long recordId, String type, String status, LocalDateTime start, LocalDateTime end) {
        return repository.findByMultipleParams(jobInstanceId, jobName, recordId, type, status, start, end);
    }

    public Page<JobLogEntry> searchByMultipleParams(Long jobInstanceId, String jobName, Long recordId, String type, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return repository.findByMultipleParams(jobInstanceId, jobName, recordId, type, status, start, end, pageable);
    }

    public Page<JobLogEntry> searchByMultipleParams(Long jobInstanceId, String jobName, Long recordId, String type, String status, LocalDateTime start, LocalDateTime end, 
                                                    int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByMultipleParams(jobInstanceId, jobName, recordId, type, status, start, end, pageable);
    }
}
