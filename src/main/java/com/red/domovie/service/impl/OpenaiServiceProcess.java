package com.red.domovie.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.service.OpenaiService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OpenaiServiceProcess implements OpenaiService{
	
	private final ChatClient chatClient;

	@Override
	public String aiAnswerProcess(QuestionDTO dto) {
		return chatClient.prompt()
        		.system("너는 영화 추천해주는 ai야. 프롬프트에 맞는 영화를 추천해줘. 200자 이내로 짧게만 설명해줘.")
        		.user(dto.getContent()).call().content();
	}

}
