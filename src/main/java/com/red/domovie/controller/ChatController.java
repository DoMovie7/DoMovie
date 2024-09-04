package com.red.domovie.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.red.domovie.domain.dto.chat.ChatMessageDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller //@messageMapping의 기본 주소는 "/message"
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        ChatRoomDTO room = chatService.findRoomById(chatMessage.getRoomId());
        messagingTemplate.convertAndSend("/topic/chatting/" + room.getRoomId(), chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        ChatRoomDTO room = chatService.findRoomById(chatMessage.getRoomId());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("room_id", room.getRoomId());
        chatMessage.setType(ChatMessageDTO.MessageType.ENTER);
        chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        messagingTemplate.convertAndSend("/topic/chatting/" + room.getRoomId(), chatMessage);
    }

    // WebSocket 연결이 끊어졌을 때 호출되는 메소드
    // 이 메소드는 별도의 이벤트 리스너 클래스에서 구현해야 합니다.
    public void handleWebSocketDisconnectListener(String username, String roomId) {
        ChatMessageDTO chatMessage = new ChatMessageDTO();
        chatMessage.setType(ChatMessageDTO.MessageType.QUIT);
        chatMessage.setSender(username);
        chatMessage.setMessage(username + "님이 퇴장했습니다.");

        messagingTemplate.convertAndSend("/topic/chatting/" + roomId, chatMessage);
    }
}