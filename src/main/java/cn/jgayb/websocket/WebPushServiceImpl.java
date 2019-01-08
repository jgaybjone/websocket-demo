package cn.jgayb.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jone.wang on 2018/7/26.
 * Description: js code
 *
 * var  wsServer = 'ws://localhost:8080/websocket?token=12345';
 * var  websocket = new WebSocket(wsServer);
 * websocket.onopen = function (evt) { onOpen(evt) };
 * websocket.onclose = function (evt) { onClose(evt) };
 * websocket.onmessage = function (evt) { onMessage(evt) };
 * websocket.onerror = function (evt) { onError(evt) };
 * function onOpen(evt) {
 *      console.log("Connected to WebSocket server.");
 * }
 * function onClose(evt) {
 *      console.log("Disconnected");
 * }
 * function onMessage(evt) {
 *      console.log('Retrieved data from server: ' + new Date() +"\ndata: " + evt.data);
 * }
 * function onError(evt) {
 *      console.log('Error occured: ' + evt.data);
 * };
 */
@Service
@Slf4j
public class WebPushServiceImpl {

    private Map<String, List<String>> msgStorage = new ConcurrentHashMap<>();


    private AtomicInteger count = new AtomicInteger(0);

    public void sendMessage(String token, String message) {
        final List<String> list = msgStorage.get(token);
        if (list != null) {
            list.add(message);
        }
    }


    public Mono<Void> handle(WebSocketSession session) {

        final String query = session.getHandshakeInfo().getUri().getQuery();
        msgStorage.put(query, new CopyOnWriteArrayList<>());

        final Flux<WebSocketMessage> generate = Flux.generate(sink -> this.process(session, sink));

        return session.send(Flux.interval(Duration.ofMillis(20))
                .zipWith(generate,
                        (time, event) -> event));

    }

    private synchronized void process(WebSocketSession session, SynchronousSink<WebSocketMessage> sink) {
        final String query = session.getHandshakeInfo().getUri().getQuery();
        List<String> list = msgStorage.get(query);
        if (CollectionUtils.isEmpty(list)) {
            final WebSocketMessage webSocketMessage = session.pongMessage(dataBufferFactory ->
                    dataBufferFactory.wrap(("hello" + count.incrementAndGet()).getBytes()));
            sink.next(webSocketMessage);
            return;
        }
        final String msg = list.get(list.size() - 1);
        list.remove(msg);
        sink.next(session.textMessage(msg));

    }

}
