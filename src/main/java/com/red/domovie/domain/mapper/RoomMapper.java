package com.red.domovie.domain.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.red.domovie.domain.dto.bot.QuestionDTO;
import com.red.domovie.domain.dto.chat.ChattingRoomDTO;

@Mapper
public interface RoomMapper {

	void saveRoom(QuestionDTO dto);
	
	List<ChattingRoomDTO> findAllRoom();

	List<ChattingRoomDTO> findByRoomId(@Param("roomId") String roomId);

	long countAllRooms();

	List<ChattingRoomDTO> findAllRooms(Map<String, Integer> params);

}
