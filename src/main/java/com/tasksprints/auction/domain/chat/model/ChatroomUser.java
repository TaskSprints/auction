package com.tasksprints.auction.domain.chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name = "chatroom_users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatroomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Long chatRoomId;

    public static ChatroomUser create(Long userId, Long chatroomId){
        return ChatroomUser.builder()
            .userId(userId)
            .chatRoomId(chatroomId)
            .build();
    }
}
