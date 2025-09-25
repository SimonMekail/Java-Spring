package com.example.Customer_Service.service;


import com.example.Customer_Service.entity.Customer;
import com.example.Customer_Service.repository.CustomerRepo;
import com.example.Customer_Service.response.CustomerResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Customer getCustomerById(String id) {
        Optional<Customer> customer = customerRepo.findById(id);
        Customer customerResponse = mapper.map(customer, Customer.class);
        return customerResponse;
    }

    public Customer getCustomerByName(String customerName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(customerName));

        Customer customer = mongoTemplate.findOne(query, Customer.class);
        Customer customerResponse = mapper.map(customer, Customer.class);
        return customerResponse;
    }

    public List< Customer > getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(null);
        return customerRepo.save(customer);
    }

    public Customer updateCustomer(Customer customer) {
        Optional < Customer > customerDb = customerRepo.findById(customer.getId());

        if (customerDb.isPresent()) {
            Customer customerUpdate = customerDb.get();
            customerUpdate.setId(customer.getId());
            customerUpdate.setName(customer.getName());
            customerUpdate.setEmail(customer.getEmail());
            customerUpdate.setAge(customer.getAge());

            customerRepo.save(customerUpdate);
            return customerUpdate;
        } else {
            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + customer.getId());
        }
    }

    public void deleteCustomer(String customerId) {
        Optional < Customer > customerDb = customerRepo.findById(customerId);

        if (customerDb.isPresent()) {
            customerRepo.delete(customerDb.get());
        }
        else {

            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + customerId);
        }

    }

}