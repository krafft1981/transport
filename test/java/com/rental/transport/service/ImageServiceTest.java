package com.rental.transport.service;

import com.rental.transport.BaseTest;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ImageServiceTest extends BaseTest {

    @Mock
    private ImageRepository repository;

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
    public void delete() {
    }

    @Test
    public void getImage() {
    }

    @Test
    public void getPage() {
    }

    @Test
    public void count() {
    }
}