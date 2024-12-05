package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Message;
import com.example.journalsystem.db.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Method to send a message using only IDs
    public void sendMessage(Long senderId, Long recipientId, String messageContent) {
        // Create the message entity using only senderId and recipientId
        Message message = new Message(senderId, recipientId, messageContent, LocalDateTime.now());
        // Save the message
        messageRepository.save(message);
    }

    // Method to get messages for a specific user (both sent and received)
    public List<Message> getMessagesForUser(Long userId) {
        // Fetch messages where the user is either the sender or the recipient
        return messageRepository.findBySenderIdOrRecipientId(userId, userId);
    }
}