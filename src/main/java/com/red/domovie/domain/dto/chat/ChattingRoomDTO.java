package com.red.domovie.domain.dto.chat;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChattingRoomDTO {
	
	private String roomId;
	private long userId;
	private long adminId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	private String userName;
	private String email;

}
