package com.crif.chatbot.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crif.chatbot.repository.LinkRepository;
import com.crif.chatbot.repository.ProjectRepository;

@RestController
public class SuggestionController {

    private final LinkRepository linkRepo;
    private final ProjectRepository projectRepo;

    public SuggestionController(LinkRepository linkRepo, ProjectRepository projectRepo) {
        this.linkRepo = linkRepo;
        this.projectRepo = projectRepo;
    }

    @GetMapping("/api/suggestions")
    public Map<String, List<String>> getSuggestions() {

        List<String> links = linkRepo.findAll()
                .stream()
                .map(l -> l.getName().toLowerCase())
                .collect(Collectors.toList());

        List<String> projects = projectRepo.findAll()
                .stream()
                .map(p -> p.getProjectName().toLowerCase())
                .collect(Collectors.toList());

        return Map.of(
                "links", links,
                "projects", projects
        );
    }
}
