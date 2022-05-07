package com.betvictor.chatdocker.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.betvictor.chatdocker.domain.ChatMessage;
import com.betvictor.chatdocker.repository.ChatMessageRepository;
import com.betvictor.chatdocker.service.MessageService;


@Service
public class MessageServiceImpl implements MessageService {
    private final ChatMessageRepository chatMessageRepository;
    
    public MessageServiceImpl(final ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }
    
    @Override
    public ChatMessage createChatMessage(final ChatMessage chatMessage) {
        return getChatMessage(chatMessage);
    }
    
    
    @Override
    public ChatMessage updateChatMessage(final ChatMessage chatMessage) {
      return getChatMessage(chatMessage);
    }
    
    @Override
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }
    
    @Override
    public ChatMessage getMessage(final String id) {
        return chatMessageRepository.findById(id)
            .orElse(null);
    }
    
    private ChatMessage getChatMessage(final ChatMessage chatMessage) {
        chatMessage.setDate(Instant.now());
        return chatMessageRepository.save(chatMessage);
    }
    
}
