package com.example.Company_Service.service;

import com.example.Company_Service.entity.B2CTransaction;
import com.example.Company_Service.entity.Company;
import com.example.Company_Service.entity.Customer;
import com.example.Company_Service.repository.B2CTransactionRepo;
import com.example.Company_Service.repository.CompanyRepo;
import com.example.Company_Service.response.CompanyResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public class B2CTransactionService {

    @Autowired
    private B2CTransactionRepo bTransactionRepo;

    @Autowired
    private ModelMapper mapper;

    public B2CTransaction getB2cTransactionById(String id) {
        Optional<B2CTransaction> b2CTransaction = bTransactionRepo.findById(id);
        B2CTransaction B2CTransactionResponse = mapper.map(b2CTransaction, B2CTransaction.class);
        return B2CTransactionResponse;
    }

    public List<B2CTransaction> getAllB2CTransaction() {
        return bTransactionRepo.findAll();
    }

    public B2CTransaction createB2CTransaction(B2CTransaction b2CTransaction) {
        b2CTransaction.setId(null);
        return bTransactionRepo.save(b2CTransaction);
    }


}
