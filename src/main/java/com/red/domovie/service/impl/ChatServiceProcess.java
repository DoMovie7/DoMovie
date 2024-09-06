package com.red.domovie.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.AnswerDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.domain.dto.chat.ChattingDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;
import com.red.domovie.domain.dto.chat.PageDTO;
import com.red.domovie.domain.mapper.RoomMapper;
import com.red.domovie.service.ChatService;

import lombok.RequiredArgsConstructor;

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

	@Override
	public PageDTO findAllChattingRoom(int page, int size) {
		int offset = (page - 1) * size;
		
		Map<String, Integer> params = new HashMap<>();
		params.put("offset", offset);
		params.put("size", size);
		
        List<ChattingRoomDTO> content = roomMapper.findAllRooms(params);
        long totalElements = roomMapper.countAllRooms();
        return new PageDTO(page, size, totalElements, content);
	}
	
	@Override
	public void saveChatQuestion(QuestionDTO dto) {
		roomMapper.saveChatQuestion(dto);
		
	}

	@Override
	public void saveChatAnswer(AnswerDTO dto) {
		roomMapper.saveChatAnswer(dto);
		
	}

	@Override
	public List<ChattingDTO> findChatByRoomId(String roomId) {
		return roomMapper.findChatByRoomId(roomId);
	}

	@Override
	public void timeUpdateRoom(String key) {
		roomMapper.updateTime(key);
		
	}

	@Override
	public ChatRoomDTO findRoomInfo(String roomId) {
		return roomMapper.findByRoomInfo(roomId);
	}

}
