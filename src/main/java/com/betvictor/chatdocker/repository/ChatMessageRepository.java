package com.betvictor.chatdocker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.betvictor.chatdocker.domain.ChatMessage;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

}
