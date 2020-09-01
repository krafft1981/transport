package com.rental.transport.entity;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name="customer",
        schema = "public",
        catalog = "relationship",
        indexes = {
                @Index(columnList = "account", name = "account_idx")
        }
)

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity extends AbstractEntity {

    private String account;
    private String firstName;
    private String lastName;
    private String family;
    private String phone;
    private Blob image;
    private Set<TransportEntity> transport = new HashSet<>();

    public CustomerEntity(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "account", unique = true, nullable = false, insertable = true, updatable = false)
    @NotEmpty
    public String getAccount() {
        return account;
    }
    
    @Basic
    @Column(name = "firstName", nullable = true, insertable = true, updatable = true)
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "lastName", nullable = true, insertable = true, updatable = true)
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "family", nullable = true, insertable = true, updatable = true)
    public String getFamily() {
        return family;
    }

    @Basic
    @Column(name = "phone", nullable = true, insertable = true, updatable = true)
    public String getPhone() {
        return phone;
    }

    @Basic
    @Column(name = "image", nullable = true, insertable = true, updatable = true)
    public Blob getImage() {
        return image;
    }

    @ManyToMany
    @JoinTable(name = "customer_transport",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "transport_id"))
    public Set<TransportEntity> getTransport() {
        return transport;
    }

    public void addTransport(TransportEntity entity) {
        transport.add(entity);
    }
}
