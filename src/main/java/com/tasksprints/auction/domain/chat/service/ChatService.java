package com.tasksprints.auction.domain.chat.service;

import com.tasksprints.auction.domain.chat.dto.ChatRequest;
import com.tasksprints.auction.domain.chat.model.Chatroom;
import com.tasksprints.auction.domain.chat.model.ChatroomUser;
import com.tasksprints.auction.domain.chat.repository.ChatroomRepository;
import com.tasksprints.auction.domain.chat.repository.ChatroomUserRepository;
import com.tasksprints.auction.domain.chat.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserRepository chatroomUserRepository;

    public Chatroom createChatroom(ChatRequest.Create command){
        /**
         * chatroom은 auction이 만들면 만들어진다.
         */
        Long auctionId = command.getAuctionId();
        Chatroom chatroom = new Chatroom();

        Chatroom savedChatroom = chatroomRepository.save(chatroom);

        return savedChatroom;
    }
    @Transactional
    public ChatroomUser enterChatroom(ChatRequest.Enter command){
        /**
         * chatroom에 참여
         */
        ChatroomUser createdChatroomUser= ChatroomUser.create(command.getUserId(), command.getChatroomId());

        ChatroomUser savedChatroomUser = chatroomUserRepository.save(createdChatroomUser);

        return savedChatroomUser;
    }

    public void sendMessage(){
    }

    public void getChatrooms(){

    }

    public void getMessagesByChatroomId(){

    }

}
