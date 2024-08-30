package com.red.domovie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{
	
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
		registry.setApplicationDestinationPrefixes("/message");
		
		// 메시지 브로커를 RabbitMQ로 설정
        // "/queue" 접두사가 붙은 메시지는 RabbitMQ로 라우팅
        /*
		registry.enableStompBrokerRelay("/queue")
                .setRelayHost("localhost")  // RabbitMQ 호스트 (기본적으로 로컬)
                .setRelayPort(61613)        // RabbitMQ STOMP 포트 (기본적으로 61613)
                .setClientLogin("guest")    // RabbitMQ 로그인 ID
                .setClientPasscode("guest") // RabbitMQ 로그인 패스워드
                .setSystemLogin("guest")    // 시스템 로그인 ID (관리자용)
                .setSystemPasscode("guest");// 시스템 로그인 패스워드 (관리자용)
       */
	}

}
