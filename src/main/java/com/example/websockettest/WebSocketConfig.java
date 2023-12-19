package com.example.websockettest;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { //STOMP

    //WebSocketConfigurer registerWebSocketHandlers : 클라이언트가 보내는 통신을 처리할 핸들러 설정 (필요에 따라 구현한 핸들러, 핸드쉐이크할 주소)

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); //api 통신 시, withSockJS() 설정을 빼야됨
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*").withSockJS(); //websocket을 지원하지 않는 브라우저에서 유사한 기능 제공
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub"); //내장 브로커 사용, prefix 여러개 전달 가능, prefix가 sub인 메세지 발행 시 브로커가 처리하여 클라이언트에게 메세지 전송
        registry.setApplicationDestinationPrefixes("/pub"); //메세지의 처리나 가공이 필요한 경우, @MessageMapping가 붙은 method(핸들러)로 바운드
    }

    // jwt
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new FilterChannelInterceptor());
    }

}