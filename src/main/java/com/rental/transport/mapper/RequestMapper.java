package com.rental.transport.mapper;

import com.rental.transport.dto.Request;
import com.rental.transport.entity.RequestEntity;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper implements AbstractMapper<RequestEntity, Request> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransportMapper transportMapper;

    @Override
    public RequestEntity toEntity(Request dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, RequestEntity.class);
    }

    @Override
    public Request toDto(RequestEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Request.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(RequestEntity.class, Request.class)
                .addMappings(m -> m.skip(Request::setId))
//                .addMappings(m -> m.skip(Request::setCustomer))
//                .addMappings(m -> m.skip(Request::setDriver))
//                .addMappings(m -> m.skip(Request::setTransport))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Request.class, RequestEntity.class)
                .addMappings(m -> m.skip(RequestEntity::setId))
//                .addMappings(m -> m.skip(RequestEntity::setCustomer))
//                .addMappings(m -> m.skip(RequestEntity::setDriver))
//                .addMappings(m -> m.skip(RequestEntity::setTransport))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(RequestEntity source, Request destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
//        destination.setCustomer(source.getCustomer());
    }

    public void mapSpecificFields(Request source, RequestEntity destination) {

        destination.setId(source.getId());

    }
}
