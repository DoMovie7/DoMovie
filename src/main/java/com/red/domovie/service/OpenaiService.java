package com.red.domovie.service;

import com.red.domovie.domain.dto.bot.QuestionDTO;

public interface OpenaiService {

	String aiAnswerProcess(QuestionDTO dto);

}
