package com.rental.transport.service;

import com.rental.transport.BaseTest;
import com.rental.transport.entity.OrderBundleRepository;
import com.rental.transport.entity.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OrderServiceTest extends BaseTest {

    @Mock
    private OrderRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void repositoryNullTest() {

        Assert.assertNotNull(repository);
    }

    @Test
    public void getByPage() {
    }

    @Test
    public void getByCalendarEvent() {
    }

    @Test
    public void getOrderRequestList() {
    }

    @Test
    public void create() {
    }

    @Test
    public void confirmOrder() {
    }

    @Test
    public void rejectOrder() {
    }
}