package com.red.domovie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	// STOMP 프로토콜을 사용하여 웹소켓 엔드포인트를 등록
	// client -> "var socket=new SockJS("/chatbot")" 서버에 웹소켓 연결 시도
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chatbot").withSockJS(); 
		//withSockJS() : 웹소켓 지원하지 않으면 sockJS 사용
		registry.addEndpoint("/chat").withSockJS();
		
	}

	//메세지 브로커의 구성 설정
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		// "/message"로 시작하는 메시지는 @MessageMapping이 붙은 메서드로 라우팅
		// 사용자->서버에 메시지보낼때 전송할때 접두사
		registry.setApplicationDestinationPrefixes("/message");
		
		// 서버에서 클라이언트로 메시지를 보낼 때 사용할 접두사
		//registry.enableSimpleBroker("/topic", "/queue", "/exchange");
		
		// 채팅방을 위한 목적지 접두사 설정
        registry.setUserDestinationPrefix("/user");
		
	}

}
