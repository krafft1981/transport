package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "customer",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "account", name = "account_idx"),
                @Index(columnList = "enable", name = "account_enabled_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends AbstractEnabledEntity {

    private String account;
    private String password;
    private Boolean confirmed = true;
    private Boolean sendEmail = false;
    private Set<ImageEntity> image = new HashSet<>();
    private Set<TransportEntity> transport = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<PropertyEntity> property = new HashSet<>();
    private Set<CalendarEntity> calendar = new HashSet<>();

    public CustomerEntity(String account, String password) {
        setAccount(account);
        setPassword(password);
    }

    @Basic
    @Column(name = "account", unique = true, nullable = false, insertable = true, updatable = false)
    @NotEmpty
    public String getAccount() {
        return account;
    }

    @Basic
    @Column(name = "password", unique = false, nullable = false, insertable = true, updatable = true)
    @NotEmpty
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "send_email", nullable = false, insertable = true, updatable = true)
    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setConfirmed() {
        confirmed = true;
    }

    @Basic
    @Column(name = "confirmed", nullable = false, insertable = true, updatable = true)
    public Boolean getConfirmed() {
        return confirmed;
    }

    @OneToMany
    public Set<ImageEntity> getImage() {
        return image;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<CalendarEntity> getCalendar() {
        return calendar;
    }

    @ManyToMany
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "transport_id", nullable = false))
    public Set<TransportEntity> getTransport() {
        return transport;
    }

    @ManyToMany
    @JoinTable(name = "customer_parking",
            joinColumns = @JoinColumn(name = "customer_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "parking_id", nullable = false)
    )
    public Set<ParkingEntity> getParking() {
        return parking;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    public Set<PropertyEntity> getProperty() {
        return property;
    }

    public void addTransport(TransportEntity entity) {
        transport.add(entity);
    }

    public void addParking(ParkingEntity entity) {
        parking.add(entity);
    }

    public void addImage(ImageEntity entity) {
        image.add(entity);
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

    public void addProperty(PropertyEntity... entryes) {

        for (int id = 0; id < entryes.length; id++)
            addProperty(entryes[id]);
    }
}
