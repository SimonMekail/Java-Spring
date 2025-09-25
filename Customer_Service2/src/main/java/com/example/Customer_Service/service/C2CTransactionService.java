package com.example.Customer_Service.service;

import com.example.Customer_Service.entity.C2CTransaction;
import com.example.Customer_Service.repository.C2CTransactionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
public class C2CTransactionService {

    @Autowired
    private C2CTransactionRepo cTransactionRepo;

    @Autowired
    private ModelMapper mapper;

    public List<C2CTransaction> getAllC2CTransaction() {
        return cTransactionRepo.findAll();
    }

    public C2CTransaction createC2CTransaction(C2CTransaction c2CTransaction) {
        c2CTransaction.setId(null);
        return cTransactionRepo.save(c2CTransaction);
    }

}
