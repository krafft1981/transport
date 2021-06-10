package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "transport")
@Table(
        name = "transport",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "transport_type_id", name = "transport_type_id_idx"),
                @Index(columnList = "enable", name = "transport_enable_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportEntity extends AbstractEnabledEntity {

    private TransportTypeEntity type;
    private Set<PropertyEntity> property = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();
    private Set<CalendarEntity> calendar = new HashSet<>();

    public TransportEntity(CustomerEntity customer, TransportTypeEntity type) {

        addCustomer(customer);
        setType(type);
        customer.getParking()
                .stream()
                .forEach(parking -> {
                    addParking(parking);
                });
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    @ManyToOne
    @JoinColumn(name = "transport_type_id", referencedColumnName = "id")
    public TransportTypeEntity getType() {
        return type;
    }

    @OneToMany
    @JoinTable(name = "transport_image",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "image_id", nullable = false)
    )
    public Set<ImageEntity> getImage() {
        return image;
    }

    @ManyToMany
    @JoinTable(name = "parking_transport",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "parking_id", nullable = false)
    )
    public Set<ParkingEntity> getParking() {
        return parking;
    }

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "transport_calendar",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "calendar_id", nullable = false)
    )
    public Set<CalendarEntity> getCalendar() {
        return calendar;
    }

    @ManyToMany
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "transport_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "customer_id", nullable = false)
    )
    public Set<CustomerEntity> getCustomer() {
        return customer;
    }

    public void addCustomer(CustomerEntity entity) {

        customer.add(entity);
    }

    public void addImage(ImageEntity entity) {

        image.add(entity);
    }

    public void addParking(ParkingEntity entity) {

        if (!getParking().isEmpty())
            parking.clear();

        parking.add(entity);
    }

    public void addProperty(PropertyEntity entity) {

        String name = entity.getType().getLogicName();
        property.add(
                property.stream()
                        .filter(propertyEntity -> propertyEntity.getType().getLogicName().equals(name))
                        .findFirst()
                        .orElse(entity)
        );
    }

    public void addProperty(PropertyEntity entity, Integer order) {

        String name = entity.getType().getLogicName();
        PropertyEntity prop = property
                .stream()
                .filter(propertyEntity -> propertyEntity.getType().getLogicName().equals(name))
                .findFirst()
                .orElse(entity);
        prop.setOrder(order);
        property.add(prop);
    }

    public void addProperty(PropertyEntity... entryes) {

        for (Integer id = 0; id < entryes.length; id++)
            addProperty(entryes[id], id);
    }

    public void addCalendar(CalendarEntity entity) {

        calendar.add(entity);
    }

    public void deleteCalendarEntity(CalendarEntity entity) {

        calendar.remove(entity);
    }
}
