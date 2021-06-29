package com.rental.transport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.NotifyEntity;
import com.rental.transport.entity.NotifyRepository;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class EventService extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private NotifyRepository notifyRepository;

    private ObjectMapper objectMapper;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        List<String> headers = session.getHandshakeHeaders().get("username");
        session.close();
        sessions.remove(session);
        System.out.println("remove Account: " + headers.get(0) + " session count: " + sessions.size());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        List<String> headers = session.getHandshakeHeaders().get("username");
        if (Objects.nonNull(headers)) {
            CustomerEntity customer = customerService.getEntity(headers.get(0));

            sessions.add(session);

            notifyRepository
                    .findByCustomerOrderById(customer)
                    .stream()
                    .forEach(entity -> {
                        try {
                            session.sendMessage(new TextMessage(entity.getRequest().asText()));
                            notifyRepository.deleteById(entity.getId());
                            System.out.println("Success send message to " + customer.getAccount());
                        } catch (Exception e) {
                            System.out.println("Failed send message to: " + customer.getAccount());
                        }
                    });

            System.out.println("append Account: " + headers.get(0) + " session count: " + sessions.size());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        if (message.getPayload().isEmpty())
            session.sendMessage(message);
        else
            System.out.println("Received garbage: " + message.getPayload());
    }

    public void sendMessage(CustomerEntity customer, String text) {

        try {
            Boolean sended = false;
            for (WebSocketSession session : sessions) {
                List<String> headers = session.getHandshakeHeaders().get("username");
                if (headers.get(0).equals(customer.getAccount())) {
                    session.sendMessage(new TextMessage(text));
                    sended = true;
                    System.out.println("Success send message to " + customer.getAccount());
                    break;
                }
            }

            if (!sended)
                throw new Exception("Failed send message to: " + customer.getAccount());

        } catch (Exception e) {

            System.out.println(e.getMessage());
            NotifyEntity entity = new NotifyEntity(customer, text);
            notifyRepository.save(entity);
        }
    }
}
