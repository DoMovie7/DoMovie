package com.red.domovie.service.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.security.CustomUserDetails;
import com.red.domovie.service.BotService;
import com.red.domovie.service.KomoranService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BotServiceProcess implements BotService{
	
	private final KomoranService komoranService;

	@Override
	public String questionProcess(QuestionDTO dto) {
		
		return "죄송합니다. 의도를 파악하지 못했습니다. 다른 단어를 입력해주세요.";
	}

}
