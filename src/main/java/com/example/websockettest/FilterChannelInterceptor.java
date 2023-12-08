package com.example.websockettest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) //Spring Security보다 인터셉터의 우선순위 높이기
public class FilterChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { //메세지가 채널로 전송되기 전에 호출되는 메소드
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //StompHeaderAccessor를 이용하여 STOMP 헤더에 접근할 수 있다.
        //클라이언트에서 커스텀 헤더에 JWT를 실어 보냈으므로 Accessor로 접근

        assert headerAccessor != null;

        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
            String token = String.valueOf(headerAccessor.getNativeHeader("Authorization").get(0));
//            token = token.replace(JwtProperties.TOKEN_PREFIX, "");

            try {
//                Integer userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
//                        .getClaim("id").asInt();

                headerAccessor.addNativeHeader("User", token);
                System.out.println("test : " + token);
            } catch (Exception e){

            }
//            } catch (TokenExpiredException e) {
//                e.printStackTrace();
//            } catch (JWTVerificationException e) {
//                e.printStackTrace();
//            }
        }
        return message;
    }
}