package com.example.journalsystem.bo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;  // Only stores the ID of the sender

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;  // Only stores the ID of the recipient

    @Column(nullable = false)
    private String content;  // The message content

    @Column(nullable = false)
    private LocalDateTime sentAt;  // Timestamp of when the message was sent

    public Message(Long senderId, Long recipientId, String content, LocalDateTime sentAt) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.sentAt = sentAt;
    }
}
