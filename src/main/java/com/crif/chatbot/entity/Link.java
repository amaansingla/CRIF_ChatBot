package com.crif.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "links",
        indexes = {
            @Index(name = "idx_links_keywords", columnList = "keywords"),
            @Index(name = "idx_links_name", columnList = "name")
        }
)
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    // Comma-separated keywords: "portal,crif,mycrif"
    private String keywords;

    public Link() {
        // Required by JPA
    }

    public Link(String name, String url, String keywords) {
        this.name = name;
        this.url = url;
        this.keywords = keywords;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
