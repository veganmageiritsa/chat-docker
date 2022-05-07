package com.betvictor.chatdocker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.betvictor.chatdocker.domain.ChatMessage;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MessageServiceTest {
    
    @Autowired
    MessageService messageService;
    
    @Test
    public void createChatMessage() {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage("Hello");
        chatMessage.setName("Anna");
        ChatMessage save = messageService.createChatMessage(chatMessage);
        Assert.assertNotNull(save.getId());
        Assert.assertEquals(chatMessage.getMessage(), save.getMessage());
        Assert.assertEquals(chatMessage.getName(), save.getName());
        ChatMessage message = messageService.getMessage(save.getId());
        Assert.assertNotNull(message);
        
        message.setMessage(" I am fine");
        var updateChatMessage = messageService.updateChatMessage(message);
        Assert.assertEquals(message.getMessage(), updateChatMessage.getMessage());
        
    }
    
}
