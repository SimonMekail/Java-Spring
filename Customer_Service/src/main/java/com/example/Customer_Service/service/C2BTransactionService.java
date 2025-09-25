package com.example.Customer_Service.service;

import com.example.Customer_Service.entity.C2BTransaction;
import com.example.Customer_Service.repository.C2BTransactionRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
public class C2BTransactionService {

    @Autowired
    private C2BTransactionRepo cTransactionRepo;

    @Autowired
    private ModelMapper mapper;

    public List<C2BTransaction> getAllC2BTransaction() {
        return cTransactionRepo.findAll();
    }

    public C2BTransaction createC2BTransaction(C2BTransaction c2BTransaction) {
        c2BTransaction.setId(null);
        return cTransactionRepo.save(c2BTransaction);
    }

}
