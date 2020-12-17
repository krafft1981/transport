package com.rental.transport.service;

import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.MessageRepository;
import com.rental.transport.mapper.MessageMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    public MessageEntity get(Long id) throws ObjectNotFoundException {

        return messageRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Message", id));
    }
}
