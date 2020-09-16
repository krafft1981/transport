package com.rental.transport;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ContextConfiguration
@Configuration
@EnableWebMvc
@ComponentScan("com.rental.transport")
public class TestConfig implements WebMvcConfigurer {


}
