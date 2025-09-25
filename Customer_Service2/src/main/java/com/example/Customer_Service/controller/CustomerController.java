package com.example.Customer_Service.controller;


import com.example.Customer_Service.entity.Customer;
import com.example.Customer_Service.response.CustomerResponse;
import com.example.Customer_Service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer/{id}")
    private ResponseEntity<Customer> getCustomerDetails(@PathVariable("id") String id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping("/customerName/{customerName}")
    public ResponseEntity<Customer> getCustomerByNameDetails(@PathVariable("customerName") String customerName) {

        Customer customer = customerService.getCustomerByName(customerName);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping("/customers")
    public ResponseEntity <List<Customer>> getAllCustomers(){

        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @PostMapping("/customer")
    public ResponseEntity < Customer > createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok().body(customerService.createCustomer(customer));
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity < Customer > updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        customer.setId(id);
        return ResponseEntity.ok().body(customerService.updateCustomer(customer));
    }

    @DeleteMapping("/customer/{id}")
    public HttpStatus deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
        return HttpStatus.OK;
    }

}