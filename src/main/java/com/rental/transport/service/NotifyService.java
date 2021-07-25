package com.rental.transport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.dto.Notify;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.NotifyEntity;
import com.rental.transport.entity.NotifyRepository;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotifyService extends TextWebSocketHandler implements HandshakeInterceptor {

    private ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotifyRepository notifyRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes)
        throws Exception {

        if (request.getHeaders().get("username").size() == 0)
            return false;

        try {
            String account = request.getHeaders().get("username").get(0);
            customerService.getEntity(account);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
        throws InterruptedException, IOException {

        if (message.getPayload().isEmpty())
            session.sendMessage(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
        throws Exception, ObjectNotFoundException {

        List<String> headers = session.getHandshakeHeaders().get("username");
        CustomerEntity customer = customerService.getEntity(headers.get(0));
        sessions.put(customer.getAccount(), session);
        notifyRepository
            .findByCustomerOrderById(customer)
            .forEach(entity -> {
                try {
                    sendMessage(entity.getCustomer(), entity.getAction(), entity.getMessage(), false);
                    notifyRepository.deleteById(entity.getId());
                } catch (Exception e) {
                    System.out.println(e);
                }
            });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        super.afterConnectionClosed(session, status);
        List<String> headers = session.getHandshakeHeaders().get("username");
        session.close();
        sessions.remove(headers.get(0));
    }

    private void sendMessage(CustomerEntity customer, String action, String text) {

        try {
            sendMessage(customer, action, text, true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendMessage(CustomerEntity customer, String action, String text, Boolean save)
        throws Exception {

        try {
            if (!sessions.contains(customer.getAccount()))
                throw new IllegalArgumentException("Пользователь отвалился (");

            Notify notify = new Notify(text, action);
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, notify);
            sessions.get(customer.getAccount()).sendMessage(new TextMessage(writer.toString()));

        } catch (Exception e) {
            if (save) {
                NotifyEntity entity = new NotifyEntity(customer, action, text);
                notifyRepository.save(entity);
            }

            throw e;
        }
    }

    public void requestCreated(RequestEntity request) {

        sendMessage(request.getDriver(), "create", "Запрос создан");
    }

    public void requestConfirmed(RequestEntity request) {

        sendMessage(request.getCustomer(), "confirm", "Запрос подтверждён");
    }

    public void requestRejected(RequestEntity request) {

        sendMessage(request.getCustomer(), "reject", "Запрос не может быть выполнен");
    }

    public void requestCanceled(CustomerEntity driver,
                                CustomerEntity customer,
                                TransportEntity transport,
                                Long day,
                                Integer[] hours) {

        sendMessage(driver, "cancel", "Запрос отменён");
    }

    public void messageCreated(CustomerEntity customer) {

        sendMessage(customer, "message", "Для вас есть новое сообщение");
    }
}
