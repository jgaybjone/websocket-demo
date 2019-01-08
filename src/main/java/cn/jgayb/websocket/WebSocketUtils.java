package cn.jgayb.websocket;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jone.wang on 2018/7/26.
 * Description:
 */
public class WebSocketUtils {
    public WebSocketUtils() {
        throw new IllegalStateException("Utils can not be new");
    }

    private static final String QUERY_PREFIX = "token=";

    private static final Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

    public static boolean setSession(WebSocketSession session) {
        if (ObjectUtils.isEmpty(session)) {
            return false;
        }
        final String query = session.getHandshakeInfo().getUri().getQuery();
        if (StringUtils.isEmpty(query)) {
            return false;
        }
        if (SESSION_MAP.containsKey(query)) {
            return false;
        }
        SESSION_MAP.putIfAbsent(session.getHandshakeInfo().getUri().getQuery(), session);
        return true;
    }

    public static WebSocketSession getSession(String token) {
        return SESSION_MAP.get(QUERY_PREFIX + token);
    }
}
