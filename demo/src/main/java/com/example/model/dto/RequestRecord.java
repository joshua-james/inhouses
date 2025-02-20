package com.example.model.dto;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestRecord {

    private final int id;
    private final AtomicInteger idCounter = new AtomicInteger();
    private final URI uri;
    private String status;

    public RequestRecord(URI uri) {
        this.id = idCounter.incrementAndGet();
        this.uri = uri;
        this.status = "PENDING";
    }

    public int getId() { return id; }
    public URI getUri() { return uri; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}