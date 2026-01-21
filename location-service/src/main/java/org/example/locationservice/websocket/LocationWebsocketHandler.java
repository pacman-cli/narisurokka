package org.example.locationservice.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.locationservice.model.LocationPing;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocationWebsocketHandler extends TextWebSocketHandler {
    private final Map<String,WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String sosId = session.getUri().getQuery().split("=")[1];
        sessions.put(sosId,session);
    }

        /**
         * Serializes location then sends if session open
         */
    public void broadcast(LocationPing locationPing){
        // Serializes location; sends if session open; handles exceptions
        try{
            String message = objectMapper.writeValueAsString(locationPing);
            WebSocketSession webSocketSession= sessions.get(locationPing.getSosId().toString());
            if(webSocketSession!=null && webSocketSession.isOpen()){
                webSocketSession.sendMessage(new TextMessage(message));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
