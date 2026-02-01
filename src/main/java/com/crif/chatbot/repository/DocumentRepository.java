package com.crif.chatbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crif.chatbot.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("""
        SELECT d FROM Document d
        WHERE LOWER(d.title) LIKE %:q%
           OR LOWER(d.projectName) LIKE %:q%
           OR LOWER(d.keywords) LIKE %:q%
    """)
    List<Document> searchByKeyword(@Param("q") String query);
}
