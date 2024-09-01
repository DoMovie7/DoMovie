package com.red.domovie.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.bot.ChatRoomDTO;
import com.red.domovie.service.ChatService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatServiceProcess implements ChatService{
	
	private final ObjectMapper objectMapper;
    private Map<String, ChatRoomDTO> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoomDTO findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoomDTO createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoomDTO chatRoom = ChatRoomDTO.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    // 새로운 메서드: 사용자가 방에 입장할 때 호출
    public void addUserToRoom(String roomId, String username) {
        ChatRoomDTO room = chatRooms.get(roomId);
        if (room != null) {
            room.addUser(username);
        }
    }

    // 새로운 메서드: 사용자가 방에서 퇴장할 때 호출
    public void removeUserFromRoom(String roomId, String username) {
        ChatRoomDTO room = chatRooms.get(roomId);
        if (room != null) {
            room.removeUser(username);
        }
    }
}
