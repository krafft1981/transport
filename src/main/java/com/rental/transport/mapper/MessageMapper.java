package com.rental.transport.mapper;

import com.rental.transport.dto.Message;
import com.rental.transport.entity.MessageEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements AbstractMapper<MessageEntity, Message> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public MessageEntity toEntity(Message dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, MessageEntity.class);
    }

    @Override
    public Message toDto(MessageEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Message.class);
    }
}
