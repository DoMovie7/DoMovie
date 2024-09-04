package com.red.domovie.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;

@Mapper
public interface RoomMapper {

	void saveRoom(QuestionDTO dto);
	
	List<ChattingRoomDTO> findAllRoom();

}
