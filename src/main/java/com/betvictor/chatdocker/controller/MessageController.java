package com.betvictor.chatdocker.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betvictor.chatdocker.domain.ChatMessage;
import com.betvictor.chatdocker.repository.ChatMessageRepository;
import com.betvictor.chatdocker.service.MessageService;


@RestController
public class MessageController {
    
    private static final Logger LOG = LoggerFactory.getLogger(MessageController.class);
    
    private final MessageService messageService;
    
    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
        LOG.info("Received Message : {} ", chatMessage);
        chatMessage.setDate(Instant.now());
        ChatMessage savedChatMessage = messageService.createChatMessage(chatMessage);
        LOG.info("Message Saved : {} ", savedChatMessage);
        return savedChatMessage;
    }
    
    @GetMapping("/messages/list")
    public List<ChatMessage> getMessages() {
        return messageService.getAllMessages();
    }
    
    @GetMapping("/messages/list/{id}")
    public ChatMessage getMessage(@PathVariable String id) {
        return messageService.getMessage(id);
    }
    
    @PutMapping("/message/{id}")
    public ChatMessage updateMessage(@PathVariable String id, @RequestBody ChatMessage chatMessage){
        return messageService.updateChatMessage(chatMessage);
    }
}
