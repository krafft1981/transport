package com.rental.transport.service;

import com.rental.transport.BaseTest;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.entity.TypeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TypeServiceTest extends BaseTest {

    @Mock
    private TypeRepository repository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void repositoryNullTest() {

        Assert.assertNotNull(repository);
    }

    @Test
    public void create() {
    }

    @Test
    public void count() {
    }

    @Test
    public void getPage() {
    }
}