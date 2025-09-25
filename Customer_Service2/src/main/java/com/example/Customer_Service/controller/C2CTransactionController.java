package com.example.Customer_Service.controller;

import com.example.Customer_Service.entity.*;
import com.example.Customer_Service.service.C2CTransactionService;
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
public class C2CTransactionController {

    @Autowired
    private C2CTransactionService c2CTransactionService;


    @Autowired
    private CustomerService customerService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private DiscoveryClient discoveryClient;

    @Autowired
    public C2CTransactionController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

//    @GetMapping("/b2bTransaction/{id}")
//    public ResponseEntity<B2BTransactionResponse> getB2BTransactionDetails(@PathVariable("id") String id) {
//
//        B2BTransactionResponse b2BTransactionResponse = companyService.getCompanyById(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body(product);
//    }

    @GetMapping("/c2cTransactions")
    public ResponseEntity<List<C2CTransaction>> getAllC2CTransaction(){

        return ResponseEntity.ok().body(c2CTransactionService.getAllC2CTransaction());
    }

    @PostMapping("/c2cTransaction")
    public ResponseEntity <C2CTransaction> createC2CTransaction(@RequestBody C2CTransaction cTransaction) {
        return ResponseEntity.ok().body(c2CTransactionService.createC2CTransaction(cTransaction));
    }

    @GetMapping("/c2cTransaction/{customerId}")
    public ResponseEntity<List<C2CTransaction>> getC2CTransactionByCustomerId( @PathVariable("customerId") String customerId) {

        Customer customer = customerService.getCustomerById(customerId);

        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("seller").is(customer),Criteria.where("buyer").is(customer));
        Query query = new Query(criteria);

//        Query query = new Query();
//        query.addCriteria(Criteria.where("seller").is(customer).orOperator(Criteria.where("buyer").is(customer)));
        List<C2CTransaction> c2CTransactions = mongoTemplate.find(query, C2CTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(c2CTransactions) ;
    }

    @GetMapping("/c2CTransactionByProduct/{productName}")
    @CircuitBreaker(name = "C2CTransactionFallBack", fallbackMethod = "getC2CTransactionByProductName_FallBack")
    public ResponseEntity<List<C2CTransaction>> getC2CTransactionByProductName(@PathVariable("productName") String productName) {

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
        List<C2CTransaction> c2CTransactions = mongoTemplate.find(query, C2CTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(c2CTransactions) ;
    }

    private ResponseEntity<List<C2CTransaction>> getC2CTransactionByProductName_FallBack(String productName, RuntimeException e ) {

        List<C2CTransaction> c2CTransactionList = new ArrayList<>();

        C2CTransaction C2CTransaction = new C2CTransaction();

        C2CTransaction.setName("Fake C2CTransaction - Product Service is not available");

        c2CTransactionList.add(C2CTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(c2CTransactionList);
    }


}
