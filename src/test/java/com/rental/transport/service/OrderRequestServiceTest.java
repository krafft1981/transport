package com.rental.transport.service;

import com.rental.transport.BaseTest;
import com.rental.transport.entity.OrderRequestRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class OrderRequestServiceTest extends BaseTest {

    @Mock
    private OrderRequestRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void repositoryNullTest() {

        Assert.assertNotNull(repository);
    }

    @Test
    public void createOrderBundle() {
    }

    @Test
    public void createOrderBranch() {
    }

    @Test
    public void deleteOrderBranch() {
    }

    @Test
    public void deleteOrderBundle() {
    }

    @Test
    public void getCustomerBranches() {
    }

    @Test
    public void checkOrderRequestQuorum() {
    }
}