package com.example.servicetools.controller;

import com.example.servicetools.model.JobLogEntry;
import com.example.servicetools.service.JobLogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job-log-entries")
public class JobLogEntryController {

    @Autowired
    private JobLogEntryService jobLogEntryService;

    // Create a new job log entry
    @PostMapping
    public ResponseEntity<JobLogEntry> createJobLogEntry(@RequestBody JobLogEntry request) {
        try {
            // Set event timestamp if not provided
            if (request.getEventTs() == null) {
                request.setEventTs(LocalDateTime.now());
            }
            
            JobLogEntry saved = jobLogEntryService.createJobLogEntry(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all job log entries
    @GetMapping
    public ResponseEntity<List<JobLogEntry>> getAllJobLogEntries() {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getAllJobLogEntries();
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get all job log entries with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<JobLogEntry>> getAllJobLogEntriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getAllJobLogEntries(
            page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entry by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobLogEntry> getJobLogEntryById(@PathVariable Long id) {
        Optional<JobLogEntry> jobLogEntry = jobLogEntryService.getJobLogEntryById(id);
        return jobLogEntry.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    // Get job log entries by job instance ID
    @GetMapping("/job-instance/{jobInstanceId}")
    public ResponseEntity<List<JobLogEntry>> getJobLogEntriesByJobInstanceId(@PathVariable Long jobInstanceId) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByJobInstanceId(jobInstanceId);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by job instance ID with pagination
    @GetMapping("/job-instance/{jobInstanceId}/paginated")
    public ResponseEntity<Page<JobLogEntry>> getJobLogEntriesByJobInstanceIdPaginated(
            @PathVariable Long jobInstanceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByJobInstanceId(
            jobInstanceId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by job name
    @GetMapping("/job-name/{jobName}")
    public ResponseEntity<List<JobLogEntry>> getJobLogEntriesByJobName(@PathVariable String jobName) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByJobName(jobName);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by job name with pagination
    @GetMapping("/job-name/{jobName}/paginated")
    public ResponseEntity<Page<JobLogEntry>> getJobLogEntriesByJobNamePaginated(
            @PathVariable String jobName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByJobName(
            jobName, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by record ID
    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<JobLogEntry>> getJobLogEntriesByRecordId(@PathVariable Long recordId) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByRecordId(recordId);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by record ID with pagination
    @GetMapping("/record/{recordId}/paginated")
    public ResponseEntity<Page<JobLogEntry>> getJobLogEntriesByRecordIdPaginated(
            @PathVariable Long recordId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByRecordId(
            recordId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<JobLogEntry>> getJobLogEntriesByType(@PathVariable String type) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByType(type);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by type with pagination
    @GetMapping("/type/{type}/paginated")
    public ResponseEntity<Page<JobLogEntry>> getJobLogEntriesByTypePaginated(
            @PathVariable String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByType(
            type, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobLogEntry>> getJobLogEntriesByStatus(@PathVariable String status) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByStatus(status);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get job log entries by status with pagination
    @GetMapping("/status/{status}/paginated")
    public ResponseEntity<Page<JobLogEntry>> getJobLogEntriesByStatusPaginated(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getJobLogEntriesByStatus(
            status, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get by event time range
    @GetMapping("/event-range")
    public ResponseEntity<List<JobLogEntry>> getByEventTsRange(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getByEventTsRange(startLdt, endLdt);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get by event time range with pagination
    @GetMapping("/event-range/paginated")
    public ResponseEntity<Page<JobLogEntry>> getByEventTsRangePaginated(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getByEventTsRange(
            startLdt, endLdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    private LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Expecting input like "2025-10-04T19:52" from datetime-local
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Get recent job log entries since a given time
    @GetMapping("/recent")
    public ResponseEntity<List<JobLogEntry>> getRecentJobLogEntries(@RequestParam LocalDateTime since) {
        List<JobLogEntry> jobLogEntries = jobLogEntryService.getRecentSince(since);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Get recent job log entries with pagination
    @GetMapping("/recent/paginated")
    public ResponseEntity<Page<JobLogEntry>> getRecentJobLogEntriesPaginated(
            @RequestParam LocalDateTime since,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.getRecentSince(
            since, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Update job log entry
    @PutMapping("/{id}")
    public ResponseEntity<JobLogEntry> updateJobLogEntry(@PathVariable Long id, @RequestBody JobLogEntry request) {
        try {
            JobLogEntry result = jobLogEntryService.updateJobLogEntry(id, request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete job log entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobLogEntry(@PathVariable Long id) {
        try {
            jobLogEntryService.deleteJobLogEntry(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete all job log entries
    @DeleteMapping
    public ResponseEntity<Void> deleteAllJobLogEntries() {
        jobLogEntryService.deleteAllJobLogEntries();
        return ResponseEntity.noContent().build();
    }

    // Get job log entry count
    @GetMapping("/count")
    public ResponseEntity<Long> getJobLogEntryCount() {
        long count = jobLogEntryService.getJobLogEntryCount();
        return ResponseEntity.ok(count);
    }

    // Search with multiple parameters (non-paginated)
    @GetMapping("/search")
    public ResponseEntity<List<JobLogEntry>> searchJobLogEntries(
            @RequestParam(required = false) Long jobInstanceId,
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) Long recordId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {
        
        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;
        
        if (start != null && !start.trim().isEmpty()) {
            startLdt = parseLocalDateTime(start);
        }
        if (end != null && !end.trim().isEmpty()) {
            endLdt = parseLocalDateTime(end);
        }
        
        List<JobLogEntry> jobLogEntries = jobLogEntryService.searchByMultipleParams(
            jobInstanceId, jobName, recordId, type, status, startLdt, endLdt);
        return ResponseEntity.ok(jobLogEntries);
    }

    // Search with multiple parameters (paginated)
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<JobLogEntry>> searchJobLogEntriesPaginated(
            @RequestParam(required = false) Long jobInstanceId,
            @RequestParam(required = false) String jobName,
            @RequestParam(required = false) Long recordId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventTs") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;
        
        if (start != null && !start.trim().isEmpty()) {
            startLdt = parseLocalDateTime(start);
        }
        if (end != null && !end.trim().isEmpty()) {
            endLdt = parseLocalDateTime(end);
        }
        
        Page<JobLogEntry> jobLogEntries = jobLogEntryService.searchByMultipleParams(
            jobInstanceId, jobName, recordId, type, status, startLdt, endLdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobLogEntries);
    }
}
