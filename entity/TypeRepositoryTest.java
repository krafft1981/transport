package com.rental.transport.entity;

import com.rental.transport.BaseTest;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.var;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TypeRepositoryTest extends BaseTest {
/*
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("transport")
            .withUsername("postgres")
            .withPassword("postgres");
*/
//    @Autowired
//    private TypeRepository repository;

    @Before
    public void setUp() throws Exception {

//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
//        hikariConfig.setUsername(postgreSQLContainer.getUsername());
//        hikariConfig.setPassword(postgreSQLContainer.getPassword());
//        var dataSource = new HikariDataSource(hikariConfig);
//        repository = new SpringJdbcFrameworkRepository(new FrameworkSimpleJdbcInsert(dataSource),
//                new JdbcTemplate(dataSource), FrameworkRowMapper.getInstance());
    }

    @Test
    public void findByName() {
    }
}