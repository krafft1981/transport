package com.rental.transport;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ComponentScan(
        basePackages = {
            "com.rental.transport"
        },
        excludeFilters = {
            @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                value = {
                        TransportApplication.class,
                        SecurityAutoConfiguration.class
                }
            )
        }
)

@ActiveProfiles()

@PropertySource(value = {
    "classpath:application.properties"
}, encoding = "UTF-8")

public class TestApplication {

}
