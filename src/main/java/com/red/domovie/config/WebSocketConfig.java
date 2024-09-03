package com.red.domovie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String loginId;
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	
	// STOMP 프로토콜을 사용하여 웹소켓 엔드포인트를 등록
	// client -> "var socket=new SockJS("/chatbot")" 서버에 웹소켓 연결 시도
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/chatbot").withSockJS(); //withSockJS() : 웹소켓 지원하지 않으면 sockJS 사용
		
	}

	//메세지 브로커의 구성 설정
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		// "/message"로 시작하는 메시지는 @MessageMapping이 붙은 메서드로 라우팅
		// 사용자->서버에 메시지보낼때 전송할때 접두사
		registry.setApplicationDestinationPrefixes("/message");
		
		//registry.enableSimpleBroker("/topic", "/queue", "/exchange");
		
		/*
		// 메시지 브로커를 RabbitMQ로 설정
        // "/exchange" 접두사가 붙은 메시지는 RabbitMQ로 라우팅
		registry.enableStompBrokerRelay("/exchange")
                .setRelayHost(host)  // RabbitMQ 호스트 (기본적으로 로컬)
                .setRelayPort(port)        // RabbitMQ STOMP 포트 (기본적으로 61613)
                .setClientLogin(loginId)    // RabbitMQ 로그인 ID
                .setClientPasscode(password); // RabbitMQ 로그인 패스워드
        */
	}

}
