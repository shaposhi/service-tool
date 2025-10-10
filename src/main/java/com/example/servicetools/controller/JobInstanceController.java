package com.example.servicetools.controller;

import com.example.servicetools.model.JobInstance;
import com.example.servicetools.service.JobInstanceService;
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
@RequestMapping("/api/job-instances")
public class JobInstanceController {

    @Autowired
    private JobInstanceService jobInstanceService;

    // Create a new job instance
    @PostMapping
    public ResponseEntity<JobInstance> createJobInstance(@RequestBody JobInstance request) {
        try {
            // Set created timestamp if not provided
            if (request.getCreated() == null) {
                request.setCreated(LocalDateTime.now());
            }
            // Set updated timestamp
            request.setUpdated(LocalDateTime.now());
            
            JobInstance saved = jobInstanceService.createJobInstance(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get all job instances
    @GetMapping
    public ResponseEntity<List<JobInstance>> getAllJobInstances() {
        List<JobInstance> jobInstances = jobInstanceService.getAllJobInstances();
        return ResponseEntity.ok(jobInstances);
    }

    // Get all job instances with pagination
    @GetMapping("/paginated")
    public ResponseEntity<Page<JobInstance>> getAllJobInstancesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobInstance> jobInstances = jobInstanceService.getAllJobInstances(
            page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    // Get job instance by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobInstance> getJobInstanceById(@PathVariable Long id) {
        Optional<JobInstance> jobInstance = jobInstanceService.getJobInstanceById(id);
        return jobInstance.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // Get job instances by name
    @GetMapping("/name/{name}")
    public ResponseEntity<List<JobInstance>> getJobInstancesByName(@PathVariable String name) {
        List<JobInstance> jobInstances = jobInstanceService.getJobInstancesByName(name);
        return ResponseEntity.ok(jobInstances);
    }

    // Get job instances by name with pagination
    @GetMapping("/name/{name}/paginated")
    public ResponseEntity<Page<JobInstance>> getJobInstancesByNamePaginated(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobInstance> jobInstances = jobInstanceService.getJobInstancesByName(
            name, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    // Get job instances by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobInstance>> getJobInstancesByStatus(@PathVariable String status) {
        List<JobInstance> jobInstances = jobInstanceService.getJobInstancesByStatus(status);
        return ResponseEntity.ok(jobInstances);
    }

    // Get job instances by status with pagination
    @GetMapping("/status/{status}/paginated")
    public ResponseEntity<Page<JobInstance>> getJobInstancesByStatusPaginated(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobInstance> jobInstances = jobInstanceService.getJobInstancesByStatus(
            status, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    // Get by created time range
    @GetMapping("/created-range")
    public ResponseEntity<List<JobInstance>> getByCreatedRange(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        List<JobInstance> jobInstances = jobInstanceService.getByCreatedRange(startLdt, endLdt);
        return ResponseEntity.ok(jobInstances);
    }

    // Get by created time range with pagination
    @GetMapping("/created-range/paginated")
    public ResponseEntity<Page<JobInstance>> getByCreatedRangePaginated(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        Page<JobInstance> jobInstances = jobInstanceService.getByCreatedRange(
            startLdt, endLdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    // Get by updated time range
    @GetMapping("/updated-range")
    public ResponseEntity<List<JobInstance>> getByUpdatedRange(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        List<JobInstance> jobInstances = jobInstanceService.getByUpdatedRange(startLdt, endLdt);
        return ResponseEntity.ok(jobInstances);
    }

    // Get by updated time range with pagination
    @GetMapping("/updated-range/paginated")
    public ResponseEntity<Page<JobInstance>> getByUpdatedRangePaginated(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updated") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        LocalDateTime startLdt = parseLocalDateTime(start);
        LocalDateTime endLdt = parseLocalDateTime(end);
        Page<JobInstance> jobInstances = jobInstanceService.getByUpdatedRange(
            startLdt, endLdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    private LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        // Expecting input like "2025-10-04T19:52" from datetime-local
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // Get recent job instances since a given time
    @GetMapping("/recent")
    public ResponseEntity<List<JobInstance>> getRecentJobInstances(@RequestParam LocalDateTime since) {
        List<JobInstance> jobInstances = jobInstanceService.getRecentSince(since);
        return ResponseEntity.ok(jobInstances);
    }

    // Get recent job instances with pagination
    @GetMapping("/recent/paginated")
    public ResponseEntity<Page<JobInstance>> getRecentJobInstancesPaginated(
            @RequestParam LocalDateTime since,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<JobInstance> jobInstances = jobInstanceService.getRecentSince(
            since, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }

    // Update job instance
    @PutMapping("/{id}")
    public ResponseEntity<JobInstance> updateJobInstance(@PathVariable Long id, @RequestBody JobInstance request) {
        try {
            JobInstance result = jobInstanceService.updateJobInstance(id, request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete job instance
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobInstance(@PathVariable Long id) {
        try {
            jobInstanceService.deleteJobInstance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete all job instances
    @DeleteMapping
    public ResponseEntity<Void> deleteAllJobInstances() {
        jobInstanceService.deleteAllJobInstances();
        return ResponseEntity.noContent().build();
    }

    // Get job instance count
    @GetMapping("/count")
    public ResponseEntity<Long> getJobInstanceCount() {
        long count = jobInstanceService.getJobInstanceCount();
        return ResponseEntity.ok(count);
    }

    // Search with multiple parameters (non-paginated)
    @GetMapping("/search")
    public ResponseEntity<List<JobInstance>> searchJobInstances(
            @RequestParam(required = false) String name,
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
        
        List<JobInstance> jobInstances = jobInstanceService.searchByMultipleParams(
            name, status, startLdt, endLdt);
        return ResponseEntity.ok(jobInstances);
    }

    // Search with multiple parameters (paginated)
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<JobInstance>> searchJobInstancesPaginated(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "created") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        LocalDateTime startLdt = null;
        LocalDateTime endLdt = null;
        
        if (start != null && !start.trim().isEmpty()) {
            startLdt = parseLocalDateTime(start);
        }
        if (end != null && !end.trim().isEmpty()) {
            endLdt = parseLocalDateTime(end);
        }
        
        Page<JobInstance> jobInstances = jobInstanceService.searchByMultipleParams(
            name, status, startLdt, endLdt, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(jobInstances);
    }
}
