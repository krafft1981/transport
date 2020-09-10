package com.rental.transport.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(
		name="parking",
		schema = "public",
		catalog = "relationship",
		indexes = {
				@Index(columnList = "address", name = "address_idx")
		}
)

@Setter
@NoArgsConstructor
public class ParkingEntity extends AbstractEntity {

	private String name = "";
	private String locality = "";
	private String address = "";
	private String description = "";

	private Double latitude = 0.0;
	private Double longitude = 0.0;

	private Set<ImageEntity> image = new HashSet<>();
	private Set<CustomerEntity> customer = new HashSet<>();
	private Set<TransportEntity> transport = new HashSet<>();

	public ParkingEntity(CustomerEntity entity) {
		addCustomer(entity);
	}

	@Basic
	@Column(name = "name", nullable = false, insertable = true, updatable = true)
	public String getName() {
		return name;
	}

	@Basic
	@Column(name = "locality", nullable = false, insertable = true, updatable = true)
	@Type(type="text")
	public String getLocality() {
		return locality;
	}

	@Basic
	@Column(name = "address", nullable = false, insertable = true, updatable = true)
	@Type(type="text")
	public String getAddress() {
		return address;
	}

	@Basic
	@Column(name = "description", nullable = false, insertable = true, updatable = true)
	@Type(type="text")
	public String getDescription() {
		return description;
	}

	@Basic
	@Column(name = "latitude", nullable = false, insertable = true, updatable = true)
	public Double getLatitude() {
		return latitude;
	}

	@Basic
	@Column(name = "longitude", nullable = false, insertable = true, updatable = true)
	public Double getLongitude() {
		return longitude;
	}

	@ManyToMany
	@JoinTable(name="customer_parking",
			joinColumns=@JoinColumn(name="parking_id", nullable = false),
			inverseJoinColumns=@JoinColumn(name="customer_id", nullable = false)
	)
	public Set<CustomerEntity> getCustomer() {
		return customer;
	}

	@ManyToMany
	@JoinTable(name="parking_transport",
			joinColumns=@JoinColumn(name="parking_id", nullable = false),
			inverseJoinColumns=@JoinColumn(name="transport_id", nullable = false)
	)
	public Set<TransportEntity> getTransport() {
		return transport;
	}

	@OneToMany
	public Set<ImageEntity> getImage() {
		return image;
	}

	public void addTransport(TransportEntity entity) {
		transport.add(entity);
	}

	public void addCustomer(CustomerEntity entity) {
		customer.add(entity);
	}

	public void addImage(ImageEntity entity) {
		image.add(entity);
	}
}
