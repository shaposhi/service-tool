package com.example.servicetools.dao;

import com.example.servicetools.model.JobLogEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobLogEntryRepository extends JpaRepository<JobLogEntry, Long> {

    // By job instance ID
    List<JobLogEntry> findByJobInstanceId(Long jobInstanceId);
    Page<JobLogEntry> findByJobInstanceId(Long jobInstanceId, Pageable pageable);

    // By job name
    List<JobLogEntry> findByJobName(String jobName);
    Page<JobLogEntry> findByJobName(String jobName, Pageable pageable);

    // By record ID
    List<JobLogEntry> findByRecordId(Long recordId);
    Page<JobLogEntry> findByRecordId(Long recordId, Pageable pageable);

    // By type
    List<JobLogEntry> findByType(String type);
    Page<JobLogEntry> findByType(String type, Pageable pageable);

    // By status
    List<JobLogEntry> findByStatus(String status);
    Page<JobLogEntry> findByStatus(String status, Pageable pageable);

    // By event time range
    List<JobLogEntry> findByEventTsBetween(LocalDateTime start, LocalDateTime end);
    Page<JobLogEntry> findByEventTsBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Recent based on event time
    @Query("SELECT j FROM JobLogEntry j WHERE j.eventTs >= :since ORDER BY j.eventTs DESC")
    List<JobLogEntry> findRecentSince(@Param("since") LocalDateTime since);
    @Query("SELECT j FROM JobLogEntry j WHERE j.eventTs >= :since ORDER BY j.eventTs DESC")
    Page<JobLogEntry> findRecentSince(@Param("since") LocalDateTime since, Pageable pageable);

    // Delete old by event time
    void deleteByEventTsBefore(LocalDateTime cutoffTime);

    // Search with multiple parameters
    @Query("SELECT j FROM JobLogEntry j WHERE " +
           "(:jobInstanceId IS NULL OR j.jobInstanceId = :jobInstanceId) AND " +
           "(:jobName IS NULL OR j.jobName = :jobName) AND " +
           "(:recordId IS NULL OR j.recordId = :recordId) AND " +
           "(:type IS NULL OR j.type = :type) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:start IS NULL OR j.eventTs >= :start) AND " +
           "(:end IS NULL OR j.eventTs <= :end)")
    List<JobLogEntry> findByMultipleParams(@Param("jobInstanceId") Long jobInstanceId,
                                           @Param("jobName") String jobName,
                                           @Param("recordId") Long recordId,
                                           @Param("type") String type,
                                           @Param("status") String status,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("SELECT j FROM JobLogEntry j WHERE " +
           "(:jobInstanceId IS NULL OR j.jobInstanceId = :jobInstanceId) AND " +
           "(:jobName IS NULL OR j.jobName = :jobName) AND " +
           "(:recordId IS NULL OR j.recordId = :recordId) AND " +
           "(:type IS NULL OR j.type = :type) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:start IS NULL OR j.eventTs >= :start) AND " +
           "(:end IS NULL OR j.eventTs <= :end)")
    Page<JobLogEntry> findByMultipleParams(@Param("jobInstanceId") Long jobInstanceId,
                                           @Param("jobName") String jobName,
                                           @Param("recordId") Long recordId,
                                           @Param("type") String type,
                                           @Param("status") String status,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end,
                                           Pageable pageable);
}
