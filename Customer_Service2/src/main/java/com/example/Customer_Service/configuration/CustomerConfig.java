package com.example.Customer_Service.configuration;

import com.example.Customer_Service.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing

public class CustomerConfig {

    @Bean
    public CustomerService CustomerBean() {
        return new CustomerService();
    }

    @Bean
    public ModelMapper modelMapperBean() {
        return new ModelMapper();
    }

}
