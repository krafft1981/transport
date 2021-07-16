package com.rental.transport.mapper;

import com.rental.transport.dto.Property;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.entity.TransportTypeRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper implements AbstractMapper<TransportEntity, Transport> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private TransportTypeRepository transportTypeRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Override
    public TransportEntity toEntity(Transport dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TransportEntity.class);
    }

    @Override
    public Transport toDto(TransportEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Transport.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(TransportEntity.class, Transport.class)
                .addMappings(m -> m.skip(Transport::setId))
                .addMappings(m -> m.skip(Transport::setCustomer))
                .addMappings(m -> m.skip(Transport::setImage))
                .addMappings(m -> m.skip(Transport::setParking))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Transport.class, TransportEntity.class)
                .addMappings(m -> m.skip(TransportEntity::setId))
                .addMappings(m -> m.skip(TransportEntity::setCustomer))
                .addMappings(m -> m.skip(TransportEntity::setImage))
                .addMappings(m -> m.skip(TransportEntity::setType))
                .addMappings(m -> m.skip(TransportEntity::setParking))
                .addMappings(m -> m.skip(TransportEntity::setProperty))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(TransportEntity source, Transport destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        source.getCustomer().forEach(customer -> destination.getCustomer().add(customer.getId()));
        source.getImage().forEach(image -> destination.getImage().add(image.getId()));
        source.getParking().forEach(parking -> destination.getParking().add(parking.getId()));
    }

    public void mapSpecificFields(Transport source, TransportEntity destination) {

        destination.setId(source.getId());

        source
                .getCustomer()
                .forEach(id -> {
                    CustomerEntity customer = customerRepository.findById(id).orElse(null);
                    if (Objects.nonNull(customer))
                        destination.addCustomer(customer);
                });

        source
                .getImage()
                .forEach(id -> {
                    ImageEntity image = imageRepository.findById(id).orElse(null);
                    if (Objects.nonNull(image))
                        destination.addImage(image);
                });

        source
                .getParking()
                .forEach(id -> {
                    ParkingEntity parking = parkingRepository.findById(id).orElse(null);
                    if (Objects.nonNull(parking))
                        destination.addParking(parking);
                });

        TransportEntity transport = transportRepository.findById(source.getId()).orElse(null);
        if (Objects.nonNull(transport)) {
            transport
                    .getProperty()
                    .forEach(entity -> {
                        Property property = source.getProperty().stream()
                                .filter(it -> it.getLogicName().equals(entity.getType().getLogicName()))
                                .findFirst()
                                .orElse(null);

                        if (Objects.nonNull(property))
                            entity.setValue(property.getValue());

                        destination.addProperty(entity);
                    });
        }
    }
}
