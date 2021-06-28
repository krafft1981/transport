package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.NotifyEntity;
import com.rental.transport.entity.NotifyRepository;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class EventHandlerService extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(EventHandlerService.class);

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NotifyRepository notifyRepository;

    @Value("${spring.request.event.lifetime}")
    private Long lifeTime;

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        session.close();
        sessions.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        List<String> headers = session.getHandshakeHeaders().get("username");
        if (Objects.nonNull(headers)) {
            CustomerEntity customer = customerService.getEntity(headers.get(0));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime() - lifeTime);

            sessions.add(session);
            notifyRepository
                    .findByCustomerOrderById(customer)
                    .stream()
                    .forEach(entity -> {
                        if (entity.getDate().before(calendar.getTime()))
                            notifyRepository.deleteById(entity.getId());

                        else{
                            try {
                                session.sendMessage(new TextMessage(entity.getText()));
                                notifyRepository.deleteById(entity.getId());
                            }
                            catch (Exception e) {

                            }
                        }
                    });
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {
    }

    public void sendMessage(CustomerEntity customer, String text) {

        try {
            for (WebSocketSession session : sessions) {
                List<String> headers = session.getHandshakeHeaders().get("username");
                if (headers.get(0).equals(customer.getAccount())) {
                    session.sendMessage(new TextMessage(text));
                    break;
                }
            }

            throw new Exception("Сообщение не удалось доставить");

        } catch (Exception e) {

            NotifyEntity entity = new NotifyEntity(customer, text);
            notifyRepository.save(entity);
        }
    }
}
