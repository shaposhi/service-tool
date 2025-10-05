# Log Notification API Endpoints

This document describes the REST API endpoints available for managing log notifications.

## Base URL
```
http://localhost:8080/api/log-notifications
```

## Endpoints

### Create Log Notification
- **POST** `/api/log-notifications`
- **Body:**
  ```json
  {
    "message": "Your log message here",
    "level": "INFO|WARN|ERROR|DEBUG",
    "source": "Source system or component"
  }
  ```

### Get All Log Notifications
- **GET** `/api/log-notifications`
- Returns all log notifications

### Get Log Notification by ID
- **GET** `/api/log-notifications/{id}`
- Returns a specific log notification

### Get Log Notifications by Level
- **GET** `/api/log-notifications/level/{level}`
- Returns all notifications with the specified level (INFO, WARN, ERROR, DEBUG)

### Get Log Notifications by Source
- **GET** `/api/log-notifications/source/{source}`
- Returns all notifications from the specified source

### Search Log Notifications
- **GET** `/api/log-notifications/search?message={searchTerm}`
- Returns notifications containing the search term in the message

### Get Recent Log Notifications
- **GET** `/api/log-notifications/recent?hours={hours}`
- Returns notifications from the last N hours (default: 24)

### Update Log Notification
- **PUT** `/api/log-notifications/{id}`
- **Body:**
  ```json
  {
    "message": "Updated message",
    "level": "INFO",
    "source": "Updated source"
  }
  ```

### Delete Log Notification
- **DELETE** `/api/log-notifications/{id}`
- Deletes a specific log notification

### Delete All Log Notifications
- **DELETE** `/api/log-notifications`
- Deletes all log notifications

### Get Log Notification Count
- **GET** `/api/log-notifications/count`
- Returns the total number of log notifications

### Get Log Notification Count by Level
- **GET** `/api/log-notifications/count-by-level`
- Returns count of notifications grouped by level

## H2 Database Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Sample Data
The application automatically initializes with sample log notifications when started for the first time.
