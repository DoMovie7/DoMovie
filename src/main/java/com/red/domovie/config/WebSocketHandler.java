package com.red.domovie.config;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.red.domovie.domain.dto.chat.ChatMessageDTO;
import com.red.domovie.domain.dto.chat.ChatRoomDTO;
import com.red.domovie.service.ChatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
//socket 통신은 서버와 클라이언트가 1:N으로 관계를 맺기 때문에 한 서버에세 여러 클라이언트가 접속할 수 있으며,
//서버에는 여러 클라이언트가 발송한 메세지를 받아서 처리해줄 Handler 작성 필요.
public class WebSocketHandler extends TextWebSocketHandler {
	
    private final ObjectMapper objectMapper;
    private final ChatService chatService;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	// WebSocket 연결이 설정되면 호출되는 메소드
    }

    
    //사용자(js에서 websocket 객체)가 send("메시지")를 실행하면 호출됨. 서버는 한 사용자가 보낸 메세지(TextMessage message)를 다른 사용자'들'에게 보냄.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	
        String payload = message.getPayload(); // 클라이언트로부터 받은 메시지 내용
        ChatMessageDTO chatMessage = objectMapper.readValue(payload, ChatMessageDTO.class); // JSON을 ChatMessage 객체로 변환
        ChatRoomDTO room = chatService.findRoomById(chatMessage.getRoomId()); // 메시지가 속한 채팅방 찾기
        Set<WebSocketSession> sessions=room.getSessions();   // 방에 있는 현재 모든 WebSocket 세션
        
        if (chatMessage.getType().equals(ChatMessageDTO.MessageType.ENTER)) {
            //사용자가 방에 입장하면  Enter메세지를 보내도록 해놓음.  이건 새로운사용자가 socket 연결한 것이랑은 다름.
            //socket연결은 이 메세지 보내기전에 이미 되어있는 상태
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");  //TALK일 경우 msg가 있을 거고, ENTER일 경우 메세지 없으니까 message set
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else if (chatMessage.getType().equals(ChatMessageDTO.MessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 퇴장했습니다..");
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
        }else {
            sendToEachSocket(sessions,message ); //입장,퇴장 아닐 때는 클라이언트로부터 온 메세지 그대로 전달.
        }
    }
    
    //같은 방에 있는 모든 사용자들에게 메세지를 보냄
    private  void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        sessions.parallelStream().forEach( roomSession -> {
            try {
                roomSession.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //javascript에서  session.close해서 연결 끊음. 그리고 이 메소드 실행.
        //session은 연결 끊긴 session을 매개변수로 이거갖고 뭐 하세요.... 하고 제공해주는 것 뿐
    }

}
