package com.red.domovie.domain.dto.chat;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDTO {

    private long roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();
    //방 한 개마다 여러 사용자들을 set 형태로 가짐
    
    @Builder
    public ChatRoomDTO(long roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}
