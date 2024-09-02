package com.red.domovie.controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.service.BotService;
import com.red.domovie.service.CategoryService;
import com.red.domovie.service.ChatService;
import com.red.domovie.service.OpenaiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.ResponseDTO;
import com.red.domovie.domain.dto.bot.ChatRoomDTO;
import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller // 기본 mapping 경로 ("/message")
@RequiredArgsConstructor
public class BotController {
	
	
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;
	
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final BotService botService;
	private final ChatService chatService;
	private final CategoryService categoryService;
	private final OpenaiService aiService;
	
	//내부적으로 STOMP의 프로토콜을 사용하여 메세지를 전송
	//@SendTo 어노테이션을 처리하는 구현 객체
	private final SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/question")
	public void question(QuestionDTO dto) throws JsonProcessingException {
		
		System.out.println(">>>client의 question :"+dto);
		String key = dto.getKey();
		String responseMessage = botService.questionProcess(dto);
		
		ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setKey(key);
        responseDTO.setMessage(responseMessage);
        
        String jsonResponse = objectMapper.writeValueAsString(responseDTO);
		
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>"+jsonResponse);
		//rabbitTemplate.convertAndSend(exchange, routingKey, jsonResponse);
		
	}
	
	
	//자주 묻는 질문
	@GetMapping("/api/categories")
	@ResponseBody
    public List<FAQDTO> getAllCategories() {
		System.out.println(categoryService.getAllFaqCategories());
        return categoryService.getAllFaqCategories();
    }
	
	
	//상담사와 채팅
	@MessageMapping("/agent")
	public void agent(QuestionDTO dto) {
		
		System.out.println(">>>채팅 문의 :"+dto);
		String key = dto.getKey();
		String responseMessage = "아직 상담사와 연결되지 않았습니다. 잠시만 기다려주세요.";
		
		messagingTemplate.convertAndSend("/topic/bot/"+key, responseMessage);
		
	}
	
	//영화추천
	@MessageMapping("/openai")
	public void openai(QuestionDTO dto) {
		
		System.out.println(">>>영화추천 :"+dto);
		String key = dto.getKey();
		String responseMessage = null;
		
		messagingTemplate.convertAndSend("/topic/bot/"+key, responseMessage);
		
	}

}
