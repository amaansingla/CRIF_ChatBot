package com.crif.chatbot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crif.chatbot.entity.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {

    @Query("""
        SELECT l FROM Link l
        WHERE LOWER(l.keywords) LIKE %:query%
    """)
    List<Link> searchByKeyword(@Param("query") String query);
}
