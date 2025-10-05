package com.example.servicetools.config;

import com.example.servicetools.model.LogNotification;
import com.example.servicetools.service.LogNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LogNotificationService logNotificationService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize with sample data if database is empty
        if (logNotificationService.getLogNotificationCount() == 0) {
            createSampleData();
        }
    }

    private void createSampleData() {
        // Create sample log notifications
        logNotificationService.createLogNotification(
            "Application started successfully", 
            "INFO", 
            "Application"
        );
        
        logNotificationService.createLogNotification(
            "Database connection established", 
            "INFO", 
            "Database"
        );
        
        logNotificationService.createLogNotification(
            "User authentication failed for user: admin", 
            "WARN", 
            "Security"
        );
        
        logNotificationService.createLogNotification(
            "Memory usage is above 80%", 
            "WARN", 
            "System"
        );
        
        logNotificationService.createLogNotification(
            "Failed to connect to external service", 
            "ERROR", 
            "External API"
        );
        
        logNotificationService.createLogNotification(
            "Critical system failure detected", 
            "ERROR", 
            "System"
        );
        
        logNotificationService.createLogNotification(
            "User logged in successfully", 
            "INFO", 
            "Authentication"
        );
        
        logNotificationService.createLogNotification(
            "Cache cleared successfully", 
            "INFO", 
            "Cache"
        );
        
        logNotificationService.createLogNotification(
            "Invalid request received", 
            "WARN", 
            "API"
        );
        
        logNotificationService.createLogNotification(
            "Backup completed successfully", 
            "INFO", 
            "Backup"
        );
        
        System.out.println("Sample data initialized with " + logNotificationService.getLogNotificationCount() + " log notifications");
    }
}
