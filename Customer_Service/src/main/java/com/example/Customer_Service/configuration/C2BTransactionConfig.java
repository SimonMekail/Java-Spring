package com.example.Customer_Service.configuration;

import com.example.Customer_Service.service.C2BTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class C2BTransactionConfig {

    @Bean
    public C2BTransactionService c2BTransactionService() {
        return new C2BTransactionService();
    }

//    @Bean
//    public ModelMapper modelMapperBean() {
//        return new ModelMapper();
//    }

}
