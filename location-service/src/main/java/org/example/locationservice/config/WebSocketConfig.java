package org.example.locationservice.config;

import org.example.locationservice.websocket.LocationWebsocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  private final LocationWebsocketHandler locationWebsocketHandler;

  public WebSocketConfig(LocationWebsocketHandler locationWebsocketHandler) {
    this.locationWebsocketHandler = locationWebsocketHandler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(locationWebsocketHandler, "/ws/location")
        .setAllowedOrigins("*");
  }
}
