package com.betvictor.chatdocker.service;

import java.util.List;

import com.betvictor.chatdocker.domain.ChatMessage;


public interface MessageService {
    
    ChatMessage createChatMessage(ChatMessage chatMessage);
    
    ChatMessage updateChatMessage(ChatMessage chatMessage);
    
    List<ChatMessage> getAllMessages();
    
    ChatMessage getMessage(String id);
    
}
