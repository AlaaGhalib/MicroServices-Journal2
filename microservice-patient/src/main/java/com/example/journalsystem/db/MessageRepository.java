package com.example.journalsystem.db;

import com.example.journalsystem.bo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByRecipientId(Long recipientId);
}