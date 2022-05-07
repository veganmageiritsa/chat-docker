package com.betvictor.chatdocker.controller;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.betvictor.chatdocker.domain.ChatMessage;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@Ignore
class MessageControllerTest {
    WebSocketClient client;
    WebSocketStompClient stompClient;
    @LocalServerPort
    private int port;
    private static final Logger logger= LoggerFactory.getLogger(MessageControllerTest.class);

    @BeforeEach
    public void setup() {
        logger.info("Setting up the tests ...");
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }
    
    
    @Test
    void sendMessage() throws Exception{
        
        BlockingQueue<ChatMessage> blockingQueue = new ArrayBlockingQueue<>(1);
        
        StompSession session = stompClient
            .connect(getWsPath(), new StompSessionHandlerAdapter() {
            })
            .get(1, SECONDS);
        
        session.subscribe("/topic/messages", new StompFrameHandler() {
            
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }
            
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received message: " + payload);
                blockingQueue.add((ChatMessage) payload);
            }
        });
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage("Hello");
        chatMessage.setName("Mike");
        session.send("/app/chat", chatMessage);
        
        assertEquals(chatMessage.getMessage(), Objects.requireNonNull(blockingQueue.poll(5, SECONDS)).getMessage());
    }
    
    private String getWsPath() {
        return String.format("ws://localhost:%d/chat", port);
    }
    
}
