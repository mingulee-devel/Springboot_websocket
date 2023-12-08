package com.example.websockettest;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController { //Message Handler
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat") // /ws/chat
    public void sendMessage(ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatDto.getChannelId(), chatDto);
    }
}