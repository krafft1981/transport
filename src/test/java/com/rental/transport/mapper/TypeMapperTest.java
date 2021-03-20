package com.rental.transport.mapper;

import com.rental.transport.BaseTest;
import com.rental.transport.utils.RandomPrimitives;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TypeMapperTest extends BaseTest {

    @Autowired
    private TypeMapper mapper;

    private Long id;
    private String name;

    @Before
    public void setUp() throws Exception {
        id = RandomPrimitives.getRandomLong();
        name = RandomPrimitives.getRandomString();
    }

    @Test
    public void toEntity() {
//        Type dto = new Type(name);
//        dto.setId(id);
//        TypeEntity entity = mapper.toEntity(dto);
//        Assert.assertEquals(name, entity.getName());
//        Assert.assertEquals(id, entity.getId());
    }

    @Test
    public void toDto() {
//        TypeEntity entity = new TypeEntity(name);
//        entity.setId(id);
//        Type dto = mapper.toDto(entity);
//        Assert.assertEquals(name, dto.getName());
//        Assert.assertEquals(id, dto.getId());
    }
}