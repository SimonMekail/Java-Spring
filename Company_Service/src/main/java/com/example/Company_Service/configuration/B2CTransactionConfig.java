package com.example.Company_Service.configuration;

import com.example.Company_Service.service.B2CTransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class B2CTransactionConfig {

    @Bean
    public B2CTransactionService b2CTransactionService() {
        return new B2CTransactionService();
    }

//    @Bean
//    public ModelMapper modelMapperBeanB2BTransaction() {
//        return new ModelMapper();
//    }

}