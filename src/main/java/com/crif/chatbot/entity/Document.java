package com.crif.chatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "documents",
        indexes = {
            @Index(name = "idx_documents_title", columnList = "title"),
            @Index(name = "idx_documents_project", columnList = "projectName"),
            @Index(name = "idx_documents_keywords", columnList = "keywords")
        }
)
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;

    private String title;

    @Column(length = 10000)
    private String content;

    private String keywords;

    public Document() {
        // Required by JPA
    }

    public Document(String projectName, String title, String content, String keywords) {
        this.projectName = projectName;
        this.title = title;
        this.content = content;
        this.keywords = keywords;
    }

    public Long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
