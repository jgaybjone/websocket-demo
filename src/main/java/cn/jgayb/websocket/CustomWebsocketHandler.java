package cn.jgayb.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * Created by jone.wang on 2018/7/26.
 * Description:
 */
@Component
public class CustomWebsocketHandler implements WebSocketHandler {

    @Autowired
    private WebPushServiceImpl webPushService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        final HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        if (handshakeInfo.getUri().getQuery() == null) {
            return session.close(CloseStatus.REQUIRED_EXTENSION);
        }
        Mono<Void> input = session.receive()
                .doOnNext(message -> {
                    final String payloadAsText = message.getPayloadAsText();
                    System.out.println(payloadAsText);
                })
                .concatMap(message -> Mono.empty())
                .then();

        Mono<Void> output = webPushService.handle(session);

        return Mono.zip(input, output).then();
    }
}
