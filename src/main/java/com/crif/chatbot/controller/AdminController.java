package com.crif.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.crif.chatbot.entity.Document;
import com.crif.chatbot.entity.Project;
import com.crif.chatbot.repository.DocumentRepository;
import com.crif.chatbot.repository.ProjectRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ProjectRepository projectRepo;
    private final DocumentRepository documentRepo;

    public AdminController(ProjectRepository projectRepo, DocumentRepository documentRepo) {
        this.projectRepo = projectRepo;
        this.documentRepo = documentRepo;
    }

    // -------- PROJECTS --------
    @GetMapping("/projects")
    public List<Project> getProjects() {
        return projectRepo.findAll();
    }

    @PostMapping("/projects")
    public Project addProject(@RequestBody Project project) {
        return projectRepo.save(project);
    }

    @PutMapping("/projects/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project updated) {
        Project p = projectRepo.findById(id).orElseThrow();
        p.setProjectName(updated.getProjectName());
        p.setDescription(updated.getDescription());
        p.setKeywords(updated.getKeywords());
        return projectRepo.save(p);
    }

    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectRepo.deleteById(id);
    }

    // -------- DOCUMENTS --------
    @GetMapping("/documents")
    public List<Document> getDocuments() {
        return documentRepo.findAll();
    }

    @PostMapping("/documents")
    public Document addDocument(@RequestBody Document doc) {
        return documentRepo.save(doc);
    }

    @PutMapping("/documents/{id}")
    public Document updateDocument(@PathVariable Long id, @RequestBody Document updated) {
        Document d = documentRepo.findById(id).orElseThrow();
        d.setProjectName(updated.getProjectName());
        d.setTitle(updated.getTitle());
        d.setContent(updated.getContent());
        d.setKeywords(updated.getKeywords());
        return documentRepo.save(d);
    }

    @DeleteMapping("/documents/{id}")
    public void deleteDocument(@PathVariable Long id) {
        documentRepo.deleteById(id);
    }
}
