package com.example.Company_Service.repository;

import com.example.Company_Service.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CompanyRepo extends MongoRepository<Company, String> {

}
