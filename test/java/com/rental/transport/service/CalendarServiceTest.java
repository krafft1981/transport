package com.rental.transport.service;

import com.rental.transport.BaseTest;
import com.rental.transport.entity.CalendarRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CalendarServiceTest extends BaseTest {

    @Mock
    private CalendarRepository repository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void repositoryNullTest() {

        Assert.assertNotNull(repository);
    }

    @Test
    public void count() {
    }

    @Test
    public void getEntityById() {
    }

    @Test
    public void putOutEvent() {
    }

    @Test
    public void deleteOutEvent() {
    }

    @Test
    public void checkCustomerBusy() {
    }

    @Test
    public void getDayEventList() {
    }

    @Test
    public void checkTransportBusy() {
    }

    @Test
    public void getTransportEventList() {
    }
}