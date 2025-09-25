package com.example.Customer_Service.repository;


import com.example.Customer_Service.entity.C2BTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface C2BTransactionRepo extends MongoRepository<C2BTransaction, String> {

}
