package com.tasksprints.auction.domain.chat.repository;

import com.tasksprints.auction.domain.chat.model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
