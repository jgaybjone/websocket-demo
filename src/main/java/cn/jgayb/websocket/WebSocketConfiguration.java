package cn.jgayb.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jone.wang on 2018/7/26.
 * Description:
 */
@Configuration
public class WebSocketConfiguration {
    /**
     * 使用 map 指定 WebSocket 协议的路由，路由为 ws://localhost:8080/echo
     */
    @Autowired
    @Bean
    public HandlerMapping webSocketMapping(final CustomWebsocketHandler websocketHandler) {
        final Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/websocket", websocketHandler);
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }

    /**
     * WebSocketHandlerAdapter 负责将 EchoHandler 处理类适配到 WebFlux 容器中
     *
     * @return WebSocketHandlerAdapter
     */
    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

}
