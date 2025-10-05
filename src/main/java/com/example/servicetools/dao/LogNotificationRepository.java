package com.example.servicetools.dao;

import com.example.servicetools.model.LogNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogNotificationRepository extends JpaRepository<LogNotification, Long> {

    // Find by level
    List<LogNotification> findByLevel(String level);
    Page<LogNotification> findByLevel(String level, Pageable pageable);

    // Find by source
    List<LogNotification> findBySource(String source);
    Page<LogNotification> findBySource(String source, Pageable pageable);

    // Find by level and source
    List<LogNotification> findByLevelAndSource(String level, String source);
    Page<LogNotification> findByLevelAndSource(String level, String source, Pageable pageable);

    // Find by timestamp range
    List<LogNotification> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    Page<LogNotification> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    // Find by message containing text
    List<LogNotification> findByMessageContainingIgnoreCase(String message);
    Page<LogNotification> findByMessageContainingIgnoreCase(String message, Pageable pageable);

    // Custom query to find recent notifications
    @Query("SELECT l FROM LogNotification l WHERE l.timestamp >= :since ORDER BY l.timestamp DESC")
    List<LogNotification> findRecentNotifications(@Param("since") LocalDateTime since);
    
    @Query("SELECT l FROM LogNotification l WHERE l.timestamp >= :since ORDER BY l.timestamp DESC")
    Page<LogNotification> findRecentNotifications(@Param("since") LocalDateTime since, Pageable pageable);

    // Custom query to count notifications by level
    @Query("SELECT l.level, COUNT(l) FROM LogNotification l GROUP BY l.level")
    List<Object[]> countByLevel();

    // Delete old notifications
    void deleteByTimestampBefore(LocalDateTime cutoffTime);
}
