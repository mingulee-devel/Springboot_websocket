package com.example.websockettest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) { //메세지가 채널로 전송되기 전에 호출되는 메소드
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //StompHeaderAccessor를 이용하여 STOMP 헤더에 접근할 수 있다.
        //클라이언트에서 커스텀 헤더에 JWT를 실어 보냈으므로 Accessor로 접근

        assert accessor != null;
        logger.info("FilterChannelInterceptor ::: preSend");

        if(StompCommand.SEND == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: SEND");
        }
        if(StompCommand.DISCONNECT == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: DISCONNECT");
        }
        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: SUBSCRIBE");
        }
        if(StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: UNSUBSCRIBE");
        }
        if(StompCommand.MESSAGE == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: MESSAGE");
        }
        if(StompCommand.ERROR == accessor.getCommand()) {
            logger.info("FilterChannelInterceptor ::: ERROR");
        }
        if (accessor.getCommand() == StompCommand.CONNECT) { // 연결 시에한 header 확인
            String token = String.valueOf(accessor.getNativeHeader("Authorization").get(0));
//            token = token.replace(JwtProperties.TOKEN_PREFIX, "");

            try {
//                Integer userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
//                        .getClaim("id").asInt();

                accessor.addNativeHeader("User", token);
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


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        logger.info("FilterChannelInterceptor ::: postSend");
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        logger.info("FilterChannelInterceptor ::: afterSendCompletion");
        ChannelInterceptor.super.afterSendCompletion(message, channel, sent, ex);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        logger.info("FilterChannelInterceptor ::: preReceive");
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        logger.info("FilterChannelInterceptor ::: afterReceiveCompletion");
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}