package com.red.domovie.config;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.red.domovie.domain.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Receiver {
	
	
	private final SimpMessagingTemplate messagingTemplate;
	
	//RabbitTemplate template 에서 전달한 메세지가 전송됨
	public void receiveMessage(ResponseDTO dto) throws JsonProcessingException {
		System.out.println(">>>>>>>>>>receiver: " + dto);
		//ResponseDTO dto = objectMapper.readValue(message, ResponseDTO.class);
		messagingTemplate.convertAndSend("/topic/bot/" + dto.getKey(), dto.getMessage());
			
		
	}

}
