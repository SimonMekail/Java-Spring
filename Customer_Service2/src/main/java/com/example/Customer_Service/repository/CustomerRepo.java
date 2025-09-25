package com.example.Customer_Service.repository;

import com.example.Customer_Service.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepo extends MongoRepository<Customer, String> {

}
