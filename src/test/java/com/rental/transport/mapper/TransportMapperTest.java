package com.rental.transport.mapper;

import com.rental.transport.BaseTest;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.utils.RandomPrimitives;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransportMapperTest extends BaseTest {

    @Autowired
    private TransportMapper mapper;

    private Long id;
    private TypeEntity type;
    private Set<PropertyEntity> property = new HashSet<>();
    private Set<ParkingEntity> parking = new HashSet<>();
    private Set<ImageEntity> image = new HashSet<>();
    private Set<CustomerEntity> customer = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        id = RandomPrimitives.getRandomLong();
    }

    @Test
    public void toEntity() {
//        Transport dto = new Transport();
//        dto.setId(id);
//        TransportEntity entity = mapper.toEntity(dto);
//        Assert.assertEquals(id, entity.getId());
    }

    @Test
    public void toDto() {
//        TransportEntity entity = new TransportEntity();
//        entity.setId(id);
//        Transport dto = mapper.toDto(entity);
//        Assert.assertEquals(id, dto.getId());
    }
}