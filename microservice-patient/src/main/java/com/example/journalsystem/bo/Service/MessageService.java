package com.example.journalsystem.bo.Service;

import com.example.journalsystem.bo.model.Message;
import com.example.journalsystem.bo.model.User;
import com.example.journalsystem.db.MessageRepository;
import com.example.journalsystem.db.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }
    public void sendMessage(Long senderId, Long recipientId, String messageContent) throws Exception {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("Sender not found"));
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new Exception("Recipient not found"));

        Message message = new Message(sender, recipient, messageContent, LocalDateTime.now());
        messageRepository.save(message);
    }

    public List<Message> getMessagesForRecipient(Long userId) {
        return messageRepository.findByRecipientId(userId);
    }
}