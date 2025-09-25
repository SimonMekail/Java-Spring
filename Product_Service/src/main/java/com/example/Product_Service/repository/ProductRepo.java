package com.example.Product_Service.repository;

import com.example.Product_Service.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepo extends MongoRepository<Product, String> {

}
