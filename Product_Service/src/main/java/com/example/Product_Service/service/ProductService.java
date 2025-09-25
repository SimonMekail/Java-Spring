package com.example.Product_Service.service;

import com.example.Product_Service.entity.Product;
import com.example.Product_Service.repository.ProductRepo;
import com.example.Product_Service.response.ProductResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Product getProductById(String id) {
        Optional<Product> product = productRepo.findById(id);
        Product productResponse = mapper.map(product, Product.class);
        return productResponse;
    }

    public Product getProductByName(String productName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(productName));

        Product product = mongoTemplate.findOne(query, Product.class);
        Product productResponse = mapper.map(product, Product.class);
        return productResponse;
    }

    public List < Product > getAllProduct() {
        return productRepo.findAll();
    }

    public Product createProduct(Product product) {
        product.setId(null);
//        product.setCreatedAt(null);
        return productRepo.save(product);
    }

    public Product updateProduct(Product product) {
        Optional < Product > productDb = productRepo.findById(product.getId());

        if (productDb.isPresent()) {
            Product productUpdate = productDb.get();
            productUpdate.setId(product.getId());
            productUpdate.setName(product.getName());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setDescription(product.getDescription());

            productRepo.save(productUpdate);
            return productUpdate;
        } else {
            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + product.getId());
        }
    }

    public void deleteProduct(String productId) {
        Optional < Product > productDb = productRepo.findById(productId);

        if (productDb.isPresent()) {
            productRepo.delete(productDb.get());
        }
        else {

            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + productId);
        }

    }
}
