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
        name = "projects",
        indexes = {
            @Index(name = "idx_projects_name", columnList = "projectName"),
            @Index(name = "idx_projects_keywords", columnList = "keywords")
        }
)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;

    @Column(length = 2000)
    private String description;

    private String keywords;

    public Project() {
        // Required by JPA
    }

    public Project(String projectName, String description, String keywords) {
        this.projectName = projectName;
        this.description = description;
        this.keywords = keywords;
    }

    public Long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
