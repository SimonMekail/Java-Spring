package com.example.Customer_Service.configuration;

import com.example.Customer_Service.service.C2CTransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class C2CTransactionConfig {

    @Bean
    public C2CTransactionService c2CTransactionService() {
        return new C2CTransactionService();
    }

//    @Bean
//    public ModelMapper modelMapperBean() {
//        return new ModelMapper();
//    }

}
