package com.example.servicetools.service;

import com.example.servicetools.dao.JobInstanceRepository;
import com.example.servicetools.model.JobInstance;
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
public class JobInstanceService {

    @Autowired
    private JobInstanceRepository repository;

    // Create operations
    public JobInstance createJobInstance(JobInstance jobInstance) {
        return repository.save(jobInstance);
    }

    public JobInstance saveJobInstance(JobInstance jobInstance) {
        return repository.save(jobInstance);
    }

    // Read operations
    public List<JobInstance> getAllJobInstances() {
        return repository.findAll();
    }

    public Page<JobInstance> getAllJobInstances(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<JobInstance> getAllJobInstances(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findAll(pageable);
    }

    public Optional<JobInstance> getJobInstanceById(Long id) {
        return repository.findById(id);
    }

    public List<JobInstance> getJobInstancesByName(String name) {
        return repository.findByName(name);
    }

    public Page<JobInstance> getJobInstancesByName(String name, Pageable pageable) {
        return repository.findByName(name, pageable);
    }

    public Page<JobInstance> getJobInstancesByName(String name, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByName(name, pageable);
    }

    public List<JobInstance> getJobInstancesByStatus(String status) {
        return repository.findByStatus(status);
    }

    public Page<JobInstance> getJobInstancesByStatus(String status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    public Page<JobInstance> getJobInstancesByStatus(String status, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByStatus(status, pageable);
    }

    public List<JobInstance> getByCreatedRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByCreatedBetween(start, end);
    }

    public Page<JobInstance> getByCreatedRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return repository.findByCreatedBetween(start, end, pageable);
    }

    public Page<JobInstance> getByCreatedRange(LocalDateTime start, LocalDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByCreatedBetween(start, end, pageable);
    }

    public List<JobInstance> getByUpdatedRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByUpdatedBetween(start, end);
    }

    public Page<JobInstance> getByUpdatedRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return repository.findByUpdatedBetween(start, end, pageable);
    }

    public Page<JobInstance> getByUpdatedRange(LocalDateTime start, LocalDateTime end, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByUpdatedBetween(start, end, pageable);
    }

    public List<JobInstance> getRecentSince(LocalDateTime since) {
        return repository.findRecentSince(since);
    }

    public Page<JobInstance> getRecentSince(LocalDateTime since, Pageable pageable) {
        return repository.findRecentSince(since, pageable);
    }

    public Page<JobInstance> getRecentSince(LocalDateTime since, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findRecentSince(since, pageable);
    }

    // Update operations
    public JobInstance updateJobInstance(Long id, JobInstance updatedJobInstance) {
        Optional<JobInstance> existingJobInstance = repository.findById(id);
        if (existingJobInstance.isPresent()) {
            JobInstance jobInstance = existingJobInstance.get();
            jobInstance.setName(updatedJobInstance.getName());
            jobInstance.setStatus(updatedJobInstance.getStatus());
            jobInstance.setUpdated(LocalDateTime.now());
            return repository.save(jobInstance);
        }
        throw new RuntimeException("JobInstance not found with id: " + id);
    }

    // Delete operations
    public void deleteJobInstance(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("JobInstance not found with id: " + id);
        }
    }

    public void deleteAllJobInstances() {
        repository.deleteAll();
    }

    public void deleteOldJobInstances(LocalDateTime cutoffTime) {
        repository.deleteByCreatedBefore(cutoffTime);
    }

    public void deleteOldJobInstancesByDays(int days) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
        repository.deleteByCreatedBefore(cutoffTime);
    }

    // Utility operations
    public long getJobInstanceCount() {
        return repository.count();
    }

    public boolean existsJobInstance(Long id) {
        return repository.existsById(id);
    }

    // Bulk operations
    public List<JobInstance> createMultipleJobInstances(List<JobInstance> jobInstances) {
        return repository.saveAll(jobInstances);
    }

    // Search with multiple parameters
    public List<JobInstance> searchByMultipleParams(String name, String status, LocalDateTime start, LocalDateTime end) {
        return repository.findByMultipleParams(name, status, start, end);
    }

    public Page<JobInstance> searchByMultipleParams(String name, String status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return repository.findByMultipleParams(name, status, start, end, pageable);
    }

    public Page<JobInstance> searchByMultipleParams(String name, String status, LocalDateTime start, LocalDateTime end, 
                                                    int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findByMultipleParams(name, status, start, end, pageable);
    }
}
