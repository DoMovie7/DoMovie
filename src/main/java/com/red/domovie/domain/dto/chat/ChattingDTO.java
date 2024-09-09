package com.red.domovie.domain.dto.chat;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChattingDTO {
	
	private String roomId;
	private String message;
	private int isUser;
	private LocalDateTime createdAt;
	
	private String userName;
	private String email;
	private String profileImageUrl;

}
