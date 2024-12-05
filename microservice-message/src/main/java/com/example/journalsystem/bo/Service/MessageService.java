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

    public void sendMessage(Long senderId, Long recipientId, String messageContent) {
        Message message = new Message(senderId, recipientId, messageContent, LocalDateTime.now());
        messageRepository.save(message);
    }
    public List<Message> getMessagesForUser(Long userId) {
        return messageRepository.findBySenderIdOrRecipientId(userId, userId);
    }
}