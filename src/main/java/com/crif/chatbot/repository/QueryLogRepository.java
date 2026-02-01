package com.crif.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crif.chatbot.entity.QueryLog;

public interface QueryLogRepository extends JpaRepository<QueryLog, Long> {}
