package com.example.servicetools.dao;

import com.example.servicetools.model.LogNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface LogNotificationRepository extends JpaRepository<LogNotification, Long> {

    // By cMode
    //List<LogNotification> findByCMode(String cMode);
    //Page<LogNotification> findByCMode(String cMode, Pageable pageable);

    // By source
    List<LogNotification> findBySource(String source);
    Page<LogNotification> findBySource(String source, Pageable pageable);

    // By partyId
    List<LogNotification> findByPartyId(Long partyId);
    Page<LogNotification> findByPartyId(Long partyId, Pageable pageable);

    // By success flag
    List<LogNotification> findBySuccesfullyProcessed(Boolean succesfullyProcessed);
    Page<LogNotification> findBySuccesfullyProcessed(Boolean succesfullyProcessed, Pageable pageable);

    // By lastUpdateTime range
    List<LogNotification> findByLastUpdateTimeBetween(ZonedDateTime start, ZonedDateTime end);
    Page<LogNotification> findByLastUpdateTimeBetween(ZonedDateTime start, ZonedDateTime end, Pageable pageable);

    // By receivedTime range
    List<LogNotification> findByReceivedTimeBetween(ZonedDateTime start, ZonedDateTime end);
    Page<LogNotification> findByReceivedTimeBetween(ZonedDateTime start, ZonedDateTime end, Pageable pageable);

    // By completedTime range
    List<LogNotification> findByCompletedTimeBetween(ZonedDateTime start, ZonedDateTime end);
    Page<LogNotification> findByCompletedTimeBetween(ZonedDateTime start, ZonedDateTime end, Pageable pageable);

    // Recent based on receivedTime
    @Query("SELECT l FROM LogNotification l WHERE l.receivedTime >= :since ORDER BY l.receivedTime DESC")
    List<LogNotification> findRecentSince(@Param("since") ZonedDateTime since);
    @Query("SELECT l FROM LogNotification l WHERE l.receivedTime >= :since ORDER BY l.receivedTime DESC")
    Page<LogNotification> findRecentSince(@Param("since") ZonedDateTime since, Pageable pageable);

    // Count by cMode
    //@Query("SELECT l.cMode, COUNT(l) FROM LogNotification l GROUP BY l.cMode")
    //List<Object[]> countByCMode();

    // Delete old by receivedTime
    void deleteByReceivedTimeBefore(ZonedDateTime cutoffTime);

    // Search with multiple parameters
    @Query("SELECT l FROM LogNotification l WHERE " +
           "(:partyId IS NULL OR l.partyId = :partyId) AND " +
           "(:success IS NULL OR l.succesfullyProcessed = :success) AND " +
           "(:source IS NULL OR l.source = :source) AND " +
           "(:cMode IS NULL OR l.cMode = :cMode) AND " +
           "(:start IS NULL OR l.receivedTime >= :start) AND " +
           "(:end IS NULL OR l.receivedTime <= :end)")
    List<LogNotification> findByMultipleParams(@Param("partyId") Long partyId,
                                               @Param("success") Boolean success,
                                               @Param("source") String source,
                                               @Param("cMode") String cMode,
                                               @Param("start") ZonedDateTime start,
                                               @Param("end") ZonedDateTime end);

    @Query("SELECT l FROM LogNotification l WHERE " +
           "(:partyId IS NULL OR l.partyId = :partyId) AND " +
           "(:success IS NULL OR l.succesfullyProcessed = :success) AND " +
           "(:source IS NULL OR l.source = :source) AND " +
           "(:cMode IS NULL OR l.cMode = :cMode) AND " +
           "(:start IS NULL OR l.receivedTime >= :start) AND " +
           "(:end IS NULL OR l.receivedTime <= :end)")
    Page<LogNotification> findByMultipleParams(@Param("partyId") Long partyId,
                                               @Param("success") Boolean success,
                                               @Param("source") String source,
                                               @Param("cMode") String cMode,
                                               @Param("start") ZonedDateTime start,
                                               @Param("end") ZonedDateTime end,
                                               Pageable pageable);
}
