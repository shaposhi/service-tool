package com.example.servicetools.config;

import com.example.servicetools.service.LogNotificationService;
import com.example.servicetools.service.ColumnToObjectMappingService;
import com.example.servicetools.model.LogNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LogNotificationService logNotificationService;

    @Autowired
    private ColumnToObjectMappingService columnToObjectMappingService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize with sample data if database is empty
        if (logNotificationService.getLogNotificationCount() == 0) {
            createSampleData();
        }
        if (columnToObjectMappingService.count() == 0) {
            createSampleMappings();
        }
    }

    private void createSampleData() {
        // Create sample log notifications aligned with new model
        ZonedDateTime now = ZonedDateTime.now();

        logNotificationService.createLogNotification(new LogNotification(
            null,           // id (auto)
            1001L,          // partyId
            "SYNC",         // cMode
            now.minusMinutes(5), // lastUpdateTime
            "Application",  // source
            now.minusMinutes(6), // receivedTime
            now.minusMinutes(4), // completedTime
            true,            // succesfullyProcessed
            null             // stackTrace
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            2002L,
            "ASYNC",
            now.minusMinutes(15),
            "Database",
            now.minusMinutes(16),
            now.minusMinutes(14),
            true,
            null
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            3003L,
            "ASYNC",
            now.minusMinutes(25),
            "Security",
            now.minusMinutes(26),
            now.minusMinutes(24),
            false,
            "com.example.security.AuthException: invalid credentials"
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            4004L,
            "SYNC",
            now.minusMinutes(35),
            "System",
            now.minusMinutes(36),
            now.minusMinutes(34),
            false,
            "java.lang.OutOfMemoryError: Java heap space"
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            5005L,
            "ASYNC",
            now.minusMinutes(45),
            "External API",
            now.minusMinutes(46),
            now.minusMinutes(44),
            false,
            "java.net.ConnectException: Connection timed out"
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            6006L,
            "SYNC",
            now.minusMinutes(55),
            "Authentication",
            now.minusMinutes(56),
            now.minusMinutes(54),
            true,
            null
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            7007L,
            "ASYNC",
            now.minusMinutes(65),
            "Cache",
            now.minusMinutes(66),
            now.minusMinutes(64),
            true,
            null
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            8008L,
            "SYNC",
            now.minusMinutes(75),
            "API",
            now.minusMinutes(76),
            now.minusMinutes(74),
            false,
            "org.example.api.BadRequestException: invalid payload"
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            9009L,
            "ASYNC",
            now.minusMinutes(85),
            "Backup",
            now.minusMinutes(86),
            now.minusMinutes(84),
            true,
            null
        ));

        logNotificationService.createLogNotification(new LogNotification(
            null,
            10010L,
            "SYNC",
            now.minusMinutes(95),
            "System",
            now.minusMinutes(96),
            now.minusMinutes(94),
            false,
            "java.lang.IllegalStateException: unexpected state"
        ));

        System.out.println("Sample data initialized with " + logNotificationService.getLogNotificationCount() + " log notifications");
    }

    private void createSampleMappings() {
        columnToObjectMappingService.create("$.user.id", "USER_ID", "UID,USR_ID");
        columnToObjectMappingService.create("$.user.name", "USER_NAME", "USERNAME,USR_NAME");
        columnToObjectMappingService.create("$.order.number", "ORDER_NO", "ORD_NUM,ORDER_ID");
        columnToObjectMappingService.create("$.order.total", "ORDER_TOTAL", "TOTAL,AMOUNT");
        columnToObjectMappingService.create("$.meta.timestamp", "EVENT_TIME", "TS,TIMESTAMP");

        columnToObjectMappingService.create("$.ce", "CE", "");
        columnToObjectMappingService.create("$.type", "CLIENT TYPE", "");
        columnToObjectMappingService.create("$.isClient", "IS CLIENT", "");
        columnToObjectMappingService.create("$.isVetted", "IS VETTED", "");
        columnToObjectMappingService.create("$.country", "COI", "TBD");
        System.out.println("Initialized 10 ColumnToObjectMapping sample records");
    }
}
