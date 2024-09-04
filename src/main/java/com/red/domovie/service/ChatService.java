package com.red.domovie.service;

import java.util.List;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;
import com.red.domovie.domain.entity.FAQEntity;

public interface ChatService {

	ChatRoomDTO findRoomById(long roomId);


	void saveRoomProcess(QuestionDTO dto);

	List<ChattingRoomDTO> findAllChatRoom();

}
