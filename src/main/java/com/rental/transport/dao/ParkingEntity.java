package com.rental.transport.dao;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name="parking", schema = "public", catalog = "relationship")
@Setter
@NoArgsConstructor
public class ParkingEntity extends EntityId {

	private String address;
	private Set<TransportEntity>  transport = new HashSet<>();

	@Basic
	@Column(name = "address", nullable = true, insertable = true, updatable = true)
	@Type(type="text")
	public String getAddress() {
		return address;
	}

	@OneToMany
	public Set<TransportEntity> getTransport() {
		return transport;
	}
}
