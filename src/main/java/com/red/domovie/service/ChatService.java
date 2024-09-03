package com.red.domovie.service;

import com.red.domovie.domain.dto.chat.ChatRoomDTO;

public interface ChatService {

	ChatRoomDTO findRoomById(long roomId);

}
