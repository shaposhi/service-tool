package com.example.servicetools.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log_notifications")
public class LogNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PARTY_ID")
    private Long partyId;

    @Column(name = "CMODE")
    private String cMode;

    @Column(name = "LAST_UPDATE_TIME")
    private ZonedDateTime lastUpdateTime;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "RECEIVED_TIME")
    private ZonedDateTime receivedTime;

    @Column(name = "COMPLETED_TIME")
    private ZonedDateTime completedTime;

    @Column(name = "IS_SUCCESSFUL")
    private Boolean succesfullyProcessed;

    @Column(name = "STACK_TRACE")
    private String stackTrace;

}
