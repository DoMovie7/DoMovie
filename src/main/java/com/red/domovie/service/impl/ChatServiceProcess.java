package com.red.domovie.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.service.ChatService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatServiceProcess implements ChatService{
	
	private final ObjectMapper objectMapper;
    private Map<Long, ChatRoomDTO> chatRooms; // 채팅방 정보를 저장할 Map입니다. 키는 방 ID, 값은 ChatRoom 객체

    @PostConstruct // 이 메서드는 의존성 주입이 완료된 후 실행
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public List<ChatRoomDTO> findAllRoom() {
        return new ArrayList<>(chatRooms.values()); // Map의 모든 값(ChatRoom 객체)을 List로 변환하여 반환
    }
    
    @Override
    public ChatRoomDTO findRoomById(long roomId) {
        return chatRooms.get(roomId); // 주어진 roomId에 해당하는 ChatRoom 객체를 반환
    }

    public ChatRoomDTO createRoom(String name) {
    	Random ran = new Random();
        long randomId = ran.nextInt(10000);
        ChatRoomDTO chatRoom = ChatRoomDTO.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom); // 생성된 채팅방을 Map에 저장
        return chatRoom; // 생성된 채팅방 객체를 반환
    }
    
    /*
    // 새로운 메서드: 사용자가 방에 입장할 때 호출
    public void addUserToRoom(long roomId, String username) {
        ChatRoomDTO room = chatRooms.get(roomId);
        if (room != null) {
            room.addUser(username);
        }
    }

    // 새로운 메서드: 사용자가 방에서 퇴장할 때 호출
    public void removeUserFromRoom(long roomId, String username) {
        ChatRoomDTO room = chatRooms.get(roomId);
        if (room != null) {
            room.removeUser(username);
        }
    }
    */
}
