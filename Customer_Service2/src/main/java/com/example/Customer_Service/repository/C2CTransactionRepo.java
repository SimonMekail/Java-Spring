package com.example.Customer_Service.repository;


import com.example.Customer_Service.entity.C2CTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface C2CTransactionRepo extends MongoRepository<C2CTransaction, String> {

}
