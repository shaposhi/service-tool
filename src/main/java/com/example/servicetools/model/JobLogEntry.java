package com.example.servicetools.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Getter
@Setter 
@Entity
@Table(name = "TJOB_LOGENTRY")
public class JobLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "JOB_INSTANCE_ID")
    private Long jobInstanceId;
    
    @Column(name = "JOB_NAME")
    private String jobName;
    
    @Column(name = "RECORD_ID")
    private Long recordId;
    
    @Column(name = "TYPE")
    private String type;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "EVENT_TS")
    private LocalDateTime eventTs;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "PAYLOAD")
    private String payload;
    
    @Column(name = "STACKTRACE")
    private String stacktrace;

}
