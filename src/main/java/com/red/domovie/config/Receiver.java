package com.red.domovie.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.ResponseDTO;
import com.red.domovie.domain.dto.bot.MessageDTO;
import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.service.KomoranService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Receiver {
	
	private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
	
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	
	private final RabbitTemplate rabbitTemplate;
	private final SimpMessagingTemplate messagingTemplate;
	private final KomoranService komoranService;
	private final TemplateEngine templateEngine; // Inject Thymeleaf template engine
	private final ObjectMapper objectMapper;
	
	//RabbitTemplate template 에서 전달한 메세지가 전송됨
	public void receiveMessage(String message) throws JsonProcessingException {
		
		try {
			ResponseDTO dto = objectMapper.readValue(message, ResponseDTO.class);
			messagingTemplate.convertAndSend("/topic/bot/" + dto.getKey(), dto.getMessage());
			
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON: {}", message, e);
            throw e;
        }
		
	}

}
