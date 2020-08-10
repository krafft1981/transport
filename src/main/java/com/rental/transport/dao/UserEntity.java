package com.rental.transport.dao;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user", schema = "public", catalog = "relationship")
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends EntityId {

    private String username;
    private String password;
    private String sirName;
    private String name;
    private String lastName;
    private String phone;
    private String role;
    private Blob image;
    private Set<TransportEntity> transport = new HashSet<>();

    @Basic
    @Column(name = "username", nullable = false, insertable = true, updatable = false)
    public String getUsername() {
        return username;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = false)
    public String getPassword() {
        return password;
    }

    @Basic
    @Column(name = "sirname", nullable = true, insertable = true, updatable = true)
    public String getSirName() {
        return sirName;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "lastName", nullable = false, insertable = true, updatable = true)
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "phone", nullable = false, insertable = true, updatable = true)
    public String getPhone() {
        return phone;
    }

    @Basic
    @Column(name = "image", nullable = false, insertable = true, updatable = true)
    public Blob getImage() {
        return image;
    }

    @Column(name = "role", nullable = false, insertable = true, updatable = false)
    public String getRole() {
        return role;
    }

    @ManyToMany
    @JoinTable(name = "user_transport",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "transport_id"))
    public Set<TransportEntity> getTransport() {
        return transport;
    }
}
