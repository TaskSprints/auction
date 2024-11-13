package com.tasksprints.auction.domain.chat.repository;

import com.tasksprints.auction.domain.chat.model.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MessageRepository extends ReactiveMongoRepository<Message, Long> {
}
