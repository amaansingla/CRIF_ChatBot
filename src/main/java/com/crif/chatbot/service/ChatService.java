package com.crif.chatbot.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.crif.chatbot.entity.Document;
import com.crif.chatbot.entity.Link;
import com.crif.chatbot.entity.Project;
import com.crif.chatbot.entity.QueryLog;
import com.crif.chatbot.repository.DocumentRepository;
import com.crif.chatbot.repository.LinkRepository;
import com.crif.chatbot.repository.ProjectRepository;
import com.crif.chatbot.repository.QueryLogRepository;

@Service
public class ChatService {

    private final LinkRepository linkRepo;
    private final ProjectRepository projectRepo;
    private final DocumentRepository docRepo;
    private final QueryLogRepository logRepo;

    // Simple in-memory cache (query -> reply)
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public ChatService(
            LinkRepository linkRepo,
            ProjectRepository projectRepo,
            DocumentRepository docRepo,
            QueryLogRepository logRepo) {

        this.linkRepo = linkRepo;
        this.projectRepo = projectRepo;
        this.docRepo = docRepo;
        this.logRepo = logRepo;
    }

    public String getReply(String message) {

        if (message == null || message.trim().isEmpty()) {
            return "Please enter a valid query.";
        }

        String q = message.toLowerCase().trim();

        // 0) Cache check
        if (cache.containsKey(q)) {
            String reply = cache.get(q);
            saveLog(message, reply, "CACHE");
            return reply;
        }

        // 1) Links search (DB + indexed)
        List<Link> links = linkRepo.searchByKeyword(q);
        if (!links.isEmpty()) {
            Link link = links.get(0);
            String reply = "✅ " + link.getName() + "\n" + link.getUrl();
            cache.put(q, reply);
            saveLog(message, reply, "DB");
            return reply;
        }

        // 2) Projects search (DB + indexed)
        List<Project> projects = projectRepo.searchByKeywordOrName(q);
        if (!projects.isEmpty()) {
            Project p = projects.get(0);
            String reply = "📌 Project: " + p.getProjectName()
                    + "\n" + p.getDescription();
            cache.put(q, reply);
            saveLog(message, reply, "DB");
            return reply;
        }

        // 3) Documents search (DB + indexed)
        List<Document> docs = docRepo.searchByKeyword(q);
        if (!docs.isEmpty()) {
            Document d = docs.get(0);

            String snippet = d.getContent();
            if (snippet.length() > 200) {
                snippet = snippet.substring(0, 200) + "...";
            }

            String reply = "📄 Document: " + d.getTitle()
                    + "\nProject: " + d.getProjectName()
                    + "\nSnippet: " + snippet;

            cache.put(q, reply);
            saveLog(message, reply, "DB");
            return reply;
        }

        // 4) Not found
        String reply = "❌ Sorry, I couldn't find relevant info.";
        saveLog(message, reply, "NOT_FOUND");
        return reply;
    }

    private void saveLog(String query, String response, String type) {
        QueryLog log = new QueryLog(query, response, type, LocalDateTime.now());
        logRepo.save(log);
    }
}
