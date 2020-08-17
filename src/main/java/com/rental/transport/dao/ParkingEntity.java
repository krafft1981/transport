package com.rental.transport.dao;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name="parking", schema = "public", catalog = "relationship", indexes = {
		@Index(columnList = "address", name = "address_idx"),
	}
)

@Setter
@NoArgsConstructor
public class ParkingEntity extends EntityId {

	private String address;
	private String description;
	private Set<CustomerEntity> customer = new HashSet<>();
	private Set<TransportEntity> transport = new HashSet<>();

	@Basic
	@Column(name = "address", nullable = true, insertable = true, updatable = true)
	@Type(type="text")
	public String getAddress() {
		return address;
	}

	@Basic
	@Column(name = "description", nullable = true, insertable = true, updatable = true)
	@Type(type="text")
	public String getDescription() {
		return description;
	}

	@OneToMany
	public Set<CustomerEntity> getCustomer() {
		return customer;
	}

	@OneToMany
	public Set<TransportEntity> getTransport() {
		return transport;
	}

	public void addTransport(TransportEntity entity) {
		transport.add(entity);
	}

	public void addCustomer(CustomerEntity entity) {
		customer.add(entity);
	}
}
