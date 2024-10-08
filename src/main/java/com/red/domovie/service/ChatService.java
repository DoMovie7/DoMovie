package com.red.domovie.service;

import java.util.List;

import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.AnswerDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.domain.dto.chat.ChattingDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;
import com.red.domovie.domain.dto.chat.PageDTO;
import com.red.domovie.domain.entity.FAQEntity;

public interface ChatService {


	void saveRoomProcess(QuestionDTO dto);

	List<ChattingRoomDTO> findAllChatRoom();

	List<ChattingRoomDTO> findByRoomId(String key);

	PageDTO findAllChattingRoom(int page, int size);

	void saveChatQuestion(QuestionDTO dto);

	void saveChatAnswer(AnswerDTO dto);

	List<ChattingDTO> findChatByRoomId(String roomId);

	void timeUpdateRoom(String key);

	ChatRoomDTO findRoomInfo(String roomId);

}
