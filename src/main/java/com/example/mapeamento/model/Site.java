package com.seuprojeto.sitemapper.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Site {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String url;

    private Integer lastStatus;
    private Long lastResponseMs;
    private Instant lastCheckedAt;
    private Boolean enabled = true;

    // getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Integer lastStatus) {
        this.lastStatus = lastStatus;
    }

    public Long getLastResponseMs() {
        return lastResponseMs;
    }

    public void setLastResponseMs(Long lastResponseMs) {
        this.lastResponseMs = lastResponseMs;
    }

    public Instant getLastCheckedAt() {
        return lastCheckedAt;
    }

    public void setLastCheckedAt(Instant lastCheckedAt) {
        this.lastCheckedAt = lastCheckedAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
