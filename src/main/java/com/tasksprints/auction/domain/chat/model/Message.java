package com.tasksprints.auction.domain.chat.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
public class Message {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private Long receiverId;
    private MessageType type;
    private String history;
    private LocalDateTime createdAt;
}
