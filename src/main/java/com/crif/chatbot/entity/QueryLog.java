package com.crif.chatbot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "query_logs")
public class QueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String queryText;

    @Column(length = 10000)
    private String responseText;

    private String responseType; // DB / CACHE / AI

    private LocalDateTime timestamp;

    public QueryLog() {}

    public QueryLog(String queryText, String responseText, String responseType, LocalDateTime timestamp) {
        this.queryText = queryText;
        this.responseText = responseText;
        this.responseType = responseType;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getQueryText() { return queryText; }
    public String getResponseText() { return responseText; }
    public String getResponseType() { return responseType; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setQueryText(String queryText) { this.queryText = queryText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }
    public void setResponseType(String responseType) { this.responseType = responseType; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
