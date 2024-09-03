package com.red.domovie.service;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.security.CustomUserDetails;

public interface BotService {

	String questionProcess(QuestionDTO dto);

}
