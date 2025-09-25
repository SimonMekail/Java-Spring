package com.example.Customer_Service.controller;

import com.example.Customer_Service.entity.C2BTransaction;
import com.example.Customer_Service.entity.Company;
import com.example.Customer_Service.entity.Customer;
import com.example.Customer_Service.entity.Product;
import com.example.Customer_Service.service.C2BTransactionService;
import com.example.Customer_Service.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/")
public class C2BTransactionController {

    @Autowired
    private C2BTransactionService c2BTransactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private DiscoveryClient discoveryClient;

    @Autowired
    public C2BTransactionController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


//    @GetMapping("/b2bTransaction/{id}")
//    public ResponseEntity<B2BTransactionResponse> getB2BTransactionDetails(@PathVariable("id") String id) {
//
//        B2BTransactionResponse b2BTransactionResponse = companyService.getCompanyById(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body(product);
//    }

    @GetMapping("/c2bTransactions")
    public ResponseEntity<List<C2BTransaction>> getAllC2BTransaction(){

        return ResponseEntity.ok().body(c2BTransactionService.getAllC2BTransaction());
    }

    @PostMapping("/c2bTransaction")
    public ResponseEntity < C2BTransaction > createC2BTransaction(@RequestBody C2BTransaction cTransaction) {
        return ResponseEntity.ok().body(c2BTransactionService.createC2BTransaction(cTransaction));
    }

    @GetMapping("/c2bTransaction/{companyId}/{customerId}")
    @CircuitBreaker(name = "C2BTransactionFallBack", fallbackMethod = "getC2BTransactionByCustomerIdAndCompanyId_FallBack")
    public ResponseEntity<List<C2BTransaction>> getC2BTransactionByCustomerIdAndCompanyId(@PathVariable("companyId") String companyId , @PathVariable("customerId") String customerId) {

        // Get service instance(s) by service name
        List<ServiceInstance> instances = discoveryClient.getInstances("company-service");

        // Choose one of the available instances (e.g., random or round-robin)
        ServiceInstance instance = instances.get(0);

        // Build the URL for the service
        String baseUrl = instance.getUri().toString();
        String url = baseUrl + "/companyService/api/company/" + companyId;

        // Make a request to the service
        RestTemplate restTemplate = new RestTemplate();

        Company company = restTemplate.getForObject(url, Company.class);

        Customer customer = customerService.getCustomerById(customerId);

        Query query = new Query();
        query.addCriteria(Criteria.where("company").is(company).and("customer").is(customer));
        List<C2BTransaction> c2BTransactions = mongoTemplate.find(query, C2BTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(c2BTransactions) ;
    }

    private ResponseEntity<List<C2BTransaction>> getC2BTransactionByCustomerIdAndCompanyId_FallBack(String companyId , String customerId , RuntimeException e ) {

        List<C2BTransaction> c2BTransactionList = new ArrayList<>();

        C2BTransaction c2BTransaction = new C2BTransaction();

        c2BTransaction.setName("Fake C2BTransaction - Company Service is not available");

        c2BTransactionList.add(c2BTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(c2BTransactionList);
    }

    @GetMapping("/c2bTransactionByProduct/{productName}")
    @CircuitBreaker(name = "C2BTransactionFallBack", fallbackMethod = "getC2BTransactionByProductName_FallBack")
    public ResponseEntity<List<C2BTransaction>> getC2BTransactionByProductName(@PathVariable("productName") String productName) {

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
        List<C2BTransaction> b2CTransactions = mongoTemplate.find(query, C2BTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(b2CTransactions) ;
    }

    private ResponseEntity<List<C2BTransaction>> getC2BTransactionByProductName_FallBack(String productName, RuntimeException e ) {

        List<C2BTransaction> c2BTransactionList = new ArrayList<>();

        C2BTransaction C2BTransaction = new C2BTransaction();

        C2BTransaction.setName("Fake C2BTransaction - Product Service is not available");

        c2BTransactionList.add(C2BTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(c2BTransactionList);
    }


}
