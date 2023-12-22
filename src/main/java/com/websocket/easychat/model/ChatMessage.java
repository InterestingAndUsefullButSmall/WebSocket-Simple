package com.websocket.easychat.model;

import lombok.Builder;

@Builder
public record ChatMessage(String content,String sender,MessageType type) {
}
