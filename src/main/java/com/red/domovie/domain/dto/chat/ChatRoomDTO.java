package com.red.domovie.domain.dto.chat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDTO {

    private String roomId;
    private long userId;
    private LocalDateTime createdAt;
    
    private String userName;
	private String email;
	private String profileImageUrl;
    
}
