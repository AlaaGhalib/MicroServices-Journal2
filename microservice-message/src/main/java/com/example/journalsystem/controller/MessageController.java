package com.example.journalsystem.controller;

import com.example.journalsystem.bo.Service.MessageService;
import com.example.journalsystem.bo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // Endpoint to send a message between users
    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestBody String messageContent) {
        try {
            messageService.sendMessage(senderId, recipientId, messageContent);
            return ResponseEntity.ok("Message sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message: " + e.getMessage());
        }
    }

    // Endpoint to get all messages involving the specified user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getMessagesForUser(@PathVariable Long userId) {
        List<Message> messages = messageService.getMessagesForUser(userId);
        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(messages);
    }
}
