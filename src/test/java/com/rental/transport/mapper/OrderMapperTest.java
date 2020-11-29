package com.rental.transport.mapper;

import com.rental.transport.BaseTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OrderMapperTest extends BaseTest {

    @Autowired
    private OrderMapper mapper;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void OrderMapperNotNullTest() {
        Assert.assertNotNull(mapper);
    }

    @Test
    public void toEntity() {
    }

    @Test
    public void toDto() {
    }
}