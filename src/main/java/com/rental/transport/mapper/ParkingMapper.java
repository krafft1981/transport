package com.rental.transport.mapper;

import com.rental.transport.dto.Parking;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParkingMapper implements AbstractMapper<ParkingEntity, Parking> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageRepository imageRepository;

    public ParkingEntity toEntity(Parking dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, ParkingEntity.class);
    }

    public Parking toDto(ParkingEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Parking.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(ParkingEntity.class, Parking.class)
                .addMappings(m -> m.skip(Parking::setId))
                .addMappings(m -> m.skip(Parking::setCustomer))
                .addMappings(m -> m.skip(Parking::setTransport))
                .addMappings(m -> m.skip(Parking::setImage))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Parking.class, ParkingEntity.class)
                .addMappings(m -> m.skip(ParkingEntity::setId))
                .addMappings(m -> m.skip(ParkingEntity::setCustomer))
                .addMappings(m -> m.skip(ParkingEntity::setTransport))
                .addMappings(m -> m.skip(ParkingEntity::setImage))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(ParkingEntity source, Parking destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        source.getCustomer().stream()
                .forEach(customer -> { destination.addCustomer(customer.getId()); });

        source.getTransport().stream()
                .forEach(transport -> { destination.addTransport(transport.getId()); });

        source.getImage().stream()
                .forEach(image -> destination.addImage(image.getId()));
    }

    public void mapSpecificFields(Parking source, ParkingEntity destination) {

        destination.setId(source.getId());

        source.getCustomer().stream()
                .forEach(id -> {
                    CustomerEntity customer = customerRepository.findById(id).orElse(null);
                    if (customer != null) {
                        destination.addCustomer(customer);
                    }
                });

        source.getTransport().stream()
                .forEach(id -> {
                    TransportEntity transport = transportRepository.findById(id).orElse(null);
                    if (transport != null) {
                        destination.addTransport(transport);
                    }
                });

        source.getImage().stream()
                .forEach(id -> {
                    ImageEntity image = imageRepository.findById(id).orElse(null);
                    if (image != null) {
                        destination.addImage(image);
                    }
                });
    }
}
