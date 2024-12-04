package com.example.journalsystem.controller;

import com.example.journalsystem.bo.Service.*;
import com.example.journalsystem.bo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/practitioners/{practitionerId}/message")
    public ResponseEntity<String> sendMessageToPractitioner(
            @PathVariable Long practitionerId,
            @RequestParam Long senderId,
            @RequestBody String messageContent) {
        try {
            messageService.sendMessage(senderId, practitionerId, messageContent);
            return ResponseEntity.ok("Message sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message: " + e.getMessage());
        }
    }

    @GetMapping("/messages/recipient")
    public ResponseEntity<List<Message>> getMessagesForRecipient(@RequestHeader("userId") Long userId) {
        List<Message> messages = messageService.getMessagesForRecipient(userId);
        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }

}

