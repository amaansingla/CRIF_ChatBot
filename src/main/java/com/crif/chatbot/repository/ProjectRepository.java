package com.crif.chatbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crif.chatbot.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
        SELECT p FROM Project p
        WHERE LOWER(p.projectName) LIKE %:query%
           OR LOWER(p.keywords) LIKE %:query%
    """)
    List<Project> searchByKeywordOrName(@Param("query") String query);
}
