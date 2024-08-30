package com.red.domovie.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BotController {
	
private final RabbitTemplate template;
	
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	
	
	// /message/bot
	@MessageMapping("/bot")
	public void bot() { //parameterType : Question dto
		//template.convertAndSend(exchange, routingKey, dto);
	}

}
