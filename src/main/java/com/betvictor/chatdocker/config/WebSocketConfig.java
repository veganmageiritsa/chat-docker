package com.betvictor.chatdocker.config;

import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			.addEndpoint("/chat")
			.setAllowedOriginPatterns("*")
			.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
//		registry.enableSimpleBroker("/queue", "/topic");
		registry.enableStompBrokerRelay("/queue", "/topic")
		        .setRelayHost("localhost") //
		        .setRelayPort(61613);;
	}
	@Bean(initMethod = "start", destroyMethod = "stop")
	public BrokerService broker() throws Exception {
		final BrokerService broker = new BrokerService();
		broker.addConnector("stomp://localhost:61613");
		
		broker.setPersistent(false);
		final ManagementContext managementContext = new ManagementContext();
		managementContext.setCreateConnector(true);
		broker.setManagementContext(managementContext);
		
		return broker;
	}
	
}
