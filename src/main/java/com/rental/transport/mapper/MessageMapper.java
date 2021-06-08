package com.rental.transport.mapper;

import com.rental.transport.dto.Message;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.MessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class MessageMapper implements AbstractMapper<MessageEntity, Message> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public MessageEntity toEntity(Message dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, MessageEntity.class);
    }

    @Override
    public Message toDto(MessageEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Message.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(MessageEntity.class, Message.class)
                .addMappings(m -> m.skip(Message::setId))
                .addMappings(m -> m.skip(Message::setCustomerId))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Message.class, MessageEntity.class)
                .addMappings(m -> m.skip(MessageEntity::setId))
                .addMappings(m -> m.skip(MessageEntity::setCustomer))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(MessageEntity source, Message destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
        destination.setCustomerId(source.getCustomer().getId());
    }

    public void mapSpecificFields(Message source, MessageEntity destination) {

        destination.setId(source.getId());
        CustomerEntity customer = customerRepository
                .findById(source.getCustomerId())
                .orElse(null);
        destination.setCustomer(customer);
    }
}
