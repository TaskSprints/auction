package com.tasksprints.auction.domain.chat.repository;

import com.tasksprints.auction.domain.chat.model.ChatroomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomUserRepository extends JpaRepository<ChatroomUser, Long> {
}
