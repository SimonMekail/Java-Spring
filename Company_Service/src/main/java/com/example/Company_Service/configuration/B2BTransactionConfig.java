package com.example.Company_Service.configuration;

import com.example.Company_Service.service.B2BTransactionService;
import com.example.Company_Service.service.CompanyService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class B2BTransactionConfig {

    @Bean
    public B2BTransactionService b2BTransactionService() {
        return new B2BTransactionService();
    }

//    @Bean
//    public ModelMapper modelMapperBeanB2BTransaction() {
//        return new ModelMapper();
//    }

}