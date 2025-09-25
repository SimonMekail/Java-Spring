package com.example.Company_Service.repository;

import com.example.Company_Service.entity.B2BTransaction;
import com.example.Company_Service.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface B2BTransactionRepo extends MongoRepository<B2BTransaction, String> {

}
