package com.rental.transport.mapper;

import com.rental.transport.dto.Customer;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements AbstractMapper<CustomerEntity, Customer> {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ModelMapper mapper;

    public CustomerEntity toEntity(Customer dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, CustomerEntity.class);
    }

    public Customer toDto(CustomerEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Customer.class);
    }

    @Override
    public CustomerEntity create() {
        CustomerEntity entity = new CustomerEntity();
        return entity;
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(CustomerEntity.class, Customer.class)
                .addMappings(m -> m.skip(Customer::setId))
                .addMappings(m -> m.skip(Customer::setTransports))
                .addMappings(m -> m.skip(Customer::setParkings))
                .addMappings(m -> m.skip(Customer::setImages))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Customer.class, CustomerEntity.class)
                .addMappings(m -> m.skip(CustomerEntity::setId))
                .addMappings(m -> m.skip(CustomerEntity::setTransport))
                .addMappings(m -> m.skip(CustomerEntity::setParking))
                .addMappings(m -> m.skip(CustomerEntity::setImage))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(CustomerEntity source, Customer destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        source.getTransport().stream()
                .forEach(transport -> { destination.addTransport(transport.getId()); });

        source.getParking().stream()
                .forEach(parking -> { destination.addParking(parking.getId()); });

        source.getImage().stream()
                .forEach(image -> { destination.addImage(image.getId()); });
    }

    public void mapSpecificFields(Customer source, CustomerEntity destination) {

        destination.setId(source.getId());

        source.getTransports().stream()
                .forEach(id -> {
                    TransportEntity transport = transportRepository.findById(id).orElse(null);
                    if (transport != null) {
                        destination.addTransport(transport);
                    }
                });

        source.getParkings().stream()
                .forEach(id -> {
                    ParkingEntity parking = parkingRepository.findById(id).orElse(null);
                    if (parking != null) {
                        destination.addParking(parking);
                    }
                });

        source.getImages().stream()
                .forEach(id -> {
                    ImageEntity image = imageRepository.findById(id).orElse(null);
                    if (image != null) {
                        destination.addImage(image);
                    }
                });
    }
}
