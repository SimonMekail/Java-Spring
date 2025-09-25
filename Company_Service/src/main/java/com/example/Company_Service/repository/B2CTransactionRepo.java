package com.example.Company_Service.repository;

import com.example.Company_Service.entity.B2CTransaction;
import com.example.Company_Service.entity.Company;
import com.example.Company_Service.entity.Customer;
import com.example.Company_Service.response.CompanyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface B2CTransactionRepo extends MongoRepository<B2CTransaction, String> {

}
