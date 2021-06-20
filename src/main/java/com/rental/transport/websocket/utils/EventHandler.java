package com.rental.transport.websocket.utils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EventHandler extends TextWebSocketHandler implements WebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();

    synchronized void addSession(WebSocketSession sess) {
        this.sessions.add(sess);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        addSession(session);
        System.out.println("New Session: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage)
            throws InterruptedException, IOException {

        for (WebSocketSession sess : sessions) {
            TextMessage msg = new TextMessage("Hello from " + session.getId() + "!");
            sess.sendMessage(msg);
        }
    }
}
