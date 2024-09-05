package com.red.domovie.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.bot.FAQDTO;
import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;
import com.red.domovie.domain.entity.FAQEntity;
import com.red.domovie.domain.repository.FaqEntityRespository;
import com.red.domovie.service.ChatService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import com.red.domovie.domain.mapper.RoomMapper;

@RequiredArgsConstructor
@Service
public class ChatServiceProcess implements ChatService{
	
    private final RoomMapper roomMapper;

	@Override
	public void saveRoomProcess(QuestionDTO dto) {
		roomMapper.saveRoom(dto);
		
	}

	@Override
	public List<ChattingRoomDTO> findAllChatRoom() {
		return roomMapper.findAllRoom();
	}

	@Override
	public List<ChattingRoomDTO> findByRoomId(String key) {
		String roomId = key;
		return roomMapper.findByRoomId(roomId);
	}

}
