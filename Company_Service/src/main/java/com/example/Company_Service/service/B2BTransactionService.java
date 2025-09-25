package com.example.Company_Service.service;

import com.example.Company_Service.entity.B2BTransaction;
import com.example.Company_Service.entity.Company;
import com.example.Company_Service.repository.B2BTransactionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class B2BTransactionService {

    @Autowired
    private B2BTransactionRepo bTransactionRepo;

    @Autowired
    private ModelMapper mapper;

    public List<B2BTransaction> getAllB2BTransaction() {
        return bTransactionRepo.findAll();
    }

    public B2BTransaction createB2BTransaction(B2BTransaction b2BTransaction) {
        b2BTransaction.setId(null);
        return bTransactionRepo.save(b2BTransaction);
    }

}
