package org.example.locationservice.websocket;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.locationservice.model.LocationPing;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LocationWebsocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
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
            sessions.put(sosId, session);
            log.info("Websocket connection established for sosId: {}", sosId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Serializes location then sends if session open
     */
    public void broadcast(LocationPing locationPing) {
        // Serializes location; sends if session open; handles exceptions
        try {
            String message = objectMapper.writeValueAsString(locationPing);
            WebSocketSession webSocketSession = sessions.get(locationPing.getSosId().toString());
            if (webSocketSession != null && webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
