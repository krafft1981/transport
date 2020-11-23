package com.rental.transport.mapper;

import com.rental.transport.BaseTest;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.utils.RandomPrimitives;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PropertyMapperTest extends BaseTest {

    @Autowired
    private PropertyMapper mapper;

    private Long id;
    private String name;
    private String value;

    @Before
    public void setUp() throws Exception {
        id = RandomPrimitives.getRandomLong();
        name = RandomPrimitives.getRandomString();
        value = RandomPrimitives.getRandomString();
    }

    @Test
    public void PropertyMapperNotNullTest() {
        Assert.assertNotNull(mapper);
    }

    @Test
    public void toEntityTest() {
        Property dto = new Property();
        dto.setId(id);
        System.out.println(dto.toString());
        PropertyEntity entity = mapper.toEntity(dto);
        Assert.assertEquals(id, entity.getId());
        Assert.assertEquals(name, entity.getName());
        Assert.assertEquals(value, entity.getValue());
    }

    @Test
    public void toDtoTest() {

        PropertyEntity entity = new PropertyEntity();
        entity.setId(id);
        Property dto = mapper.toDto(entity);
        Assert.assertEquals(id, dto.getId());
        Assert.assertEquals(name, dto.getName());
        Assert.assertEquals(value, dto.getValue());
    }
}