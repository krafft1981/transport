package com.rental.transport.config;

import com.rental.transport.websocket.utils.EventHandler;
import com.rental.transport.websocket.utils.EventInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new EventHandler(), "/websocket")
                .addInterceptors(new EventInterceptor())
                .setAllowedOrigins("*");
    }
}
