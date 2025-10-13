package com.example.servicetools.dto;

import java.util.Date;

public class IngesterResponse {
    private String id;
    private String uuid;
    private Date timestamp;

    public IngesterResponse() {
    }

    public IngesterResponse(String id, String uuid, Date timestamp) {
        this.id = id;
        this.uuid = uuid;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}