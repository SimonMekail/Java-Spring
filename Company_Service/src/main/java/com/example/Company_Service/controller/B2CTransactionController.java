package com.example.Company_Service.controller;

import com.example.Company_Service.entity.*;
import com.example.Company_Service.repository.B2CTransactionRepo;
import com.example.Company_Service.response.CompanyResponse;
import com.example.Company_Service.service.B2CTransactionService;
import com.example.Company_Service.service.CompanyService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class B2CTransactionController {

    @Autowired
    private B2CTransactionService b2CTransactionService;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private  MongoTemplate mongoTemplate;

    private DiscoveryClient discoveryClient;

    @Autowired
    public B2CTransactionController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/b2cTransaction/{id}")
    public ResponseEntity<B2CTransaction> getB2CTransactionDetails(@PathVariable("id") String id) {

        B2CTransaction b2CTransactionResponse = b2CTransactionService.getB2cTransactionById(id);

        return ResponseEntity.status(HttpStatus.OK).body(b2CTransactionResponse);
    }

    @GetMapping("/b2cTransactions")
    public ResponseEntity<List<B2CTransaction>> getAllB2CTransaction(){

        return ResponseEntity.ok().body(b2CTransactionService.getAllB2CTransaction());
    }

    @PostMapping("/b2cTransaction")
    public ResponseEntity < B2CTransaction > createB2CTransaction(@RequestBody B2CTransaction bTransaction) {
        return ResponseEntity.ok().body(b2CTransactionService.createB2CTransaction(bTransaction));
    }

//    @GetMapping("/b2cTransaction/{companyId}/{customerId}")
//    @CircuitBreaker(name = "B2CTransactionFallBack", fallbackMethod = "getB2CTransactionDetails_FallBack")
//    public ResponseEntity<List<B2CTransaction>> getB2CTransactionDetails(@PathVariable("companyId") String companyId , @PathVariable("customerId") String customerId) {
//
//        String uri = "http://localhost:8002/customerService/api/customer/" + customerId;
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        Customer customer = restTemplate.getForObject(uri, Customer.class);
//
//        Company company = companyService.getCompanyById(companyId);
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("company").is(company).and("customer").is(customer));
//        List<B2CTransaction> b2CTransactions = mongoTemplate.find(query, B2CTransaction.class);
//
//        return ResponseEntity.status(HttpStatus.OK).body(b2CTransactions) ;
//    }

    @GetMapping("/b2cTransaction/{companyId}/{customerId}")
    @CircuitBreaker(name = "B2CTransactionFallBack", fallbackMethod = "getB2CTransactionByCustomerIdAndCompanyId_FallBack")
    public ResponseEntity<List<B2CTransaction>> getB2CTransactionByCustomerIdAndCompanyId(@PathVariable("companyId") String companyId , @PathVariable("customerId") String customerId) {

        // Get service instance(s) by service name
        List<ServiceInstance> instances = discoveryClient.getInstances("customer-service");

        // Choose one of the available instances (e.g., random or round-robin)
        ServiceInstance instance = instances.get(0);

        // Build the URL for the service
        String baseUrl = instance.getUri().toString();
        String url = baseUrl + "/customerService/api/customer/" + customerId;

        // Make a request to the service
        RestTemplate restTemplate = new RestTemplate();

        Customer customer = restTemplate.getForObject(url, Customer.class);

        Company company = companyService.getCompanyById(companyId);

        Query query = new Query();
        query.addCriteria(Criteria.where("company").is(company).and("customer").is(customer));
        List<B2CTransaction> b2CTransactions = mongoTemplate.find(query, B2CTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(b2CTransactions) ;
    }

    private ResponseEntity<List<B2CTransaction>> getB2CTransactionByCustomerIdAndCompanyId_FallBack(String companyId , String customerId , RuntimeException e ) {

        List<B2CTransaction> b2CTransactionList = new ArrayList<>();

        B2CTransaction b2CTransaction = new B2CTransaction();

        b2CTransaction.setName("Fake B2CTransaction - Customer Service is not available");

        b2CTransactionList.add(b2CTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(b2CTransactionList);
    }

    @GetMapping("/b2cTransactionByProduct/{productName}")
    @CircuitBreaker(name = "B2CTransactionFallBack", fallbackMethod = "getB2CTransactionByProductName_FallBack")
    public ResponseEntity<List<B2CTransaction>> getB2CTransactionByProductName(@PathVariable("productName") String productName) {

        // Get service instance(s) by service name
        List<ServiceInstance> instances = discoveryClient.getInstances("product-service");

        // Choose one of the available instances (e.g., random or round-robin)
        ServiceInstance instance = instances.get(0);

        // Build the URL for the service
        String baseUrl = instance.getUri().toString();
        String url = baseUrl + "/productService/api/productName/" + productName;

        // Make a request to the service
        RestTemplate restTemplate = new RestTemplate();

        Product product = restTemplate.getForObject(url, Product.class);

        Query query = new Query();
        query.addCriteria(Criteria.where("product").is(product));
        List<B2CTransaction> b2CTransactions = mongoTemplate.find(query, B2CTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(b2CTransactions) ;
    }

    private ResponseEntity<List<B2CTransaction>> getB2CTransactionByProductName_FallBack(String productName, RuntimeException e ) {

        List<B2CTransaction> b2CTransactionList = new ArrayList<>();

        B2CTransaction b2CTransaction = new B2CTransaction();

        b2CTransaction.setName("Fake B2CTransaction - Product Service is not available");

        b2CTransactionList.add(b2CTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(b2CTransactionList);
    }


}
