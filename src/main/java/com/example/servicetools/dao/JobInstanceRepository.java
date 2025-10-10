package com.example.servicetools.dao;

import com.example.servicetools.model.JobInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobInstanceRepository extends JpaRepository<JobInstance, Long> {

    // By name
    List<JobInstance> findByName(String name);
    Page<JobInstance> findByName(String name, Pageable pageable);

    // By status
    List<JobInstance> findByStatus(String status);
    Page<JobInstance> findByStatus(String status, Pageable pageable);

    // By created time range
    List<JobInstance> findByCreatedBetween(LocalDateTime start, LocalDateTime end);
    Page<JobInstance> findByCreatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // By updated time range
    List<JobInstance> findByUpdatedBetween(LocalDateTime start, LocalDateTime end);
    Page<JobInstance> findByUpdatedBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Recent based on created time
    @Query("SELECT j FROM JobInstance j WHERE j.created >= :since ORDER BY j.created DESC")
    List<JobInstance> findRecentSince(@Param("since") LocalDateTime since);
    @Query("SELECT j FROM JobInstance j WHERE j.created >= :since ORDER BY j.created DESC")
    Page<JobInstance> findRecentSince(@Param("since") LocalDateTime since, Pageable pageable);

    // Delete old by created time
    void deleteByCreatedBefore(LocalDateTime cutoffTime);

    // Search with multiple parameters
    @Query("SELECT j FROM JobInstance j WHERE " +
           "(:name IS NULL OR j.name = :name) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:start IS NULL OR j.created >= :start) AND " +
           "(:end IS NULL OR j.created <= :end)")
    List<JobInstance> findByMultipleParams(@Param("name") String name,
                                          @Param("status") String status,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);

    @Query("SELECT j FROM JobInstance j WHERE " +
           "(:name IS NULL OR j.name = :name) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:start IS NULL OR j.created >= :start) AND " +
           "(:end IS NULL OR j.created <= :end)")
    Page<JobInstance> findByMultipleParams(@Param("name") String name,
                                          @Param("status") String status,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          Pageable pageable);
}
