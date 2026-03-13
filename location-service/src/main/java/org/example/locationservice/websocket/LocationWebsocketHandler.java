package org.example.locationservice.websocket;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.example.locationservice.model.LocationPing;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LocationWebsocketHandler extends TextWebSocketHandler {
    private final Map<String, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // BUG (fixed) — WebSocket NullPointerException Crash (HIGH)
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            URI uri = session.getUri();
            if (uri == null || uri.getQuery() == null) {
                log.warn("Websocket connection rejected: no query parameters");
                session.close();
                return;
            }
            // parse query parameters
            String[] params = uri.getQuery().split("&");
            String sosId = null;
            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2 && "sosId".equals(keyValue[0])) {
                    sosId = keyValue[1];
                }
            }
            if (sosId == null || sosId.isBlank()) {
                log.warn("Websocket connection rejected: missing sosId parameter");
                session.close();
                return;
            }
            sessions.computeIfAbsent(sosId, k -> ConcurrentHashMap.newKeySet()).add(session);
            log.info("Websocket connection established for sosId: {}", sosId);
        } catch (Exception e) {
            log.error("Failed to broadcast location for sosId={}", session.getId(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        sessions.values().forEach(set -> set.remove(session));

        log.info("Websocket connection closed for session: {}", session.getId());
    }

    /**
     * Serializes location then sends if session open
     */
    public void broadcast(LocationPing locationPing) {
        // Serializes location; sends if session open; handles exceptions
        try {
            String message = objectMapper.writeValueAsString(locationPing);
            Set<WebSocketSession> webSocketSessions = sessions.get(locationPing.getSosId().toString());
            if (webSocketSessions != null) {
                for (WebSocketSession ws : webSocketSessions) {
                    if (ws.isOpen()) {
                        ws.sendMessage(new TextMessage(message));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to broadcast location for sosId={}", locationPing.getSosId(), e);

        }
    }
}
