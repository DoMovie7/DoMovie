package com.red.domovie.service;

import com.red.domovie.domain.dto.bot.MessageDTO;

public interface KomoranService {

	MessageDTO nlpAnalyze(String content);

}
