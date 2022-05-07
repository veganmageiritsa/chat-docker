package com.betvictor.chatdocker.integration;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@Ignore
class WebSocketIntegrationTest {
    WebSocketClient client;
    WebSocketStompClient stompClient;
    @LocalServerPort
    private int port = 0;
    private static final Logger logger= LoggerFactory.getLogger(WebSocketIntegrationTest.class);
    
    @BeforeEach
    public void setup() {
        logger.info("Setting up the tests ...");
        client = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(new SockJsClient(
            List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }
    
    @Test
    void givenWebSocket_whenMessage_thenVerifyMessage() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Throwable> failure = new AtomicReference<>();
        StompSessionHandler sessionHandler = new StompSessionHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }
            
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                logger.info("payload:{} ", payload);
            }
            
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                logger.info("Connected to the WebSocket ...");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage("Hello");
                chatMessage.setName("Mike");
                session.send("/app/chat", chatMessage);
                session.subscribe("topic/messages", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return ChatMessage.class;
                    }
                    
                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        try {
                            
                            assertThat(payload).isNotNull();
                            assertThat(payload).isInstanceOf(ChatMessage.class);
                            
                           
                        } catch (Throwable t) {
                            failure.set(t);
                            logger.error("There is an exception ", t);
                        } finally {
                            session.disconnect();
                            latch.countDown();
                        }
                        
                    }
                });
                
            }
    
            @Override
            public void handleException(
                final StompSession session, final StompCommand command, final StompHeaders headers, final byte[] payload, final Throwable exception) {
        
            }
    
            @Override
            public void handleTransportError(final StompSession session, final Throwable exception) {
        
            }
    
        };
        stompClient.connect(getWsPath(), new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(final StompHeaders headers) {
                return super.getPayloadType(headers);
            }
    
            @Override
            public void handleFrame(final StompHeaders headers, final Object payload) {
                super.handleFrame(headers, payload);
            }
        }).get(1, TimeUnit.SECONDS);
        if (latch.await(20, TimeUnit.SECONDS)) {
            if (failure.get() != null) {
                fail("Assertion Failed", failure.get());
            }
        } else {
            fail("Could not receive the message on time");
        }
    }
    
    private String getWsPath() {
        return String.format("ws://localhost:%d/chat", port);
    }
    
}
