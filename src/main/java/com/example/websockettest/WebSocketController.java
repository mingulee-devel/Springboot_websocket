package com.example.websockettest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController { //Message Handler
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @MessageMapping("/chat") // /ws/chat
    public void sendMessage(ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        logger.info("ChatController ::: sendMessage");
//        accessor.setSessionId(String.valueOf(chatDto.getWriterId()));

        simpMessagingTemplate.convertAndSend("/sub/chat/" + chatDto.getChannelId(), chatDto);
        logger.info("Message sent successfully!");
    }


}