package com.websocket.easychat.config;

import com.websocket.easychat.model.ChatMessage;
import com.websocket.easychat.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final SimpMessageSendingOperations messageTemplate;

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String userName = (String) Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes()).get("userName");
		if (userName != null) {
			log.info("User disconnected: {}", userName);
			var chatMessage = ChatMessage.builder()
					.type(MessageType.LEAVE)
					.sender(userName)
					.build();
			messageTemplate.convertAndSend("/topic/public", chatMessage);
		}
	}
}
