package com.example.Company_Service.controller;

import com.example.Company_Service.entity.*;
import com.example.Company_Service.response.B2BTransactionResponse;
import com.example.Company_Service.response.CompanyResponse;
import com.example.Company_Service.service.B2BTransactionService;
import com.example.Company_Service.service.CompanyService;
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
public class B2BTransactionController {

    @Autowired
    private B2BTransactionService b2BTransactionService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private DiscoveryClient discoveryClient;

    @Autowired
    public B2BTransactionController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

//    @GetMapping("/b2bTransaction/{id}")
//    public ResponseEntity<B2BTransactionResponse> getB2BTransactionDetails(@PathVariable("id") String id) {
//
//        B2BTransactionResponse b2BTransactionResponse = companyService.getCompanyById(id);
//
//        return ResponseEntity.status(HttpStatus.OK).body(product);
//    }

    @GetMapping("/b2bTransactions")
    public ResponseEntity <List<B2BTransaction>> getAllB2BTransaction(){

        return ResponseEntity.ok().body(b2BTransactionService.getAllB2BTransaction());
    }

    @PostMapping("/b2bTransaction")
    public ResponseEntity < B2BTransaction > createB2BTransaction(@RequestBody B2BTransaction bTransaction) {
        return ResponseEntity.ok().body(b2BTransactionService.createB2BTransaction(bTransaction));
    }

    @GetMapping("/b2bTransaction/{companyId}")
    public ResponseEntity<List<B2BTransaction>> getB2BTransactionByCompanyId(@PathVariable("companyId") String companyId) {

        Company company = companyService.getCompanyById(companyId);

        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("sellerCompany").is(company),Criteria.where("buyerCompany").is(company));
        Query query = new Query(criteria);

//        Query query = new Query();
//        query.addCriteria(Criteria.where("sellerCompany").is(company).orOperator(Criteria.where("buyerCompany").is(company)));
        List<B2BTransaction> b2BTransactions = mongoTemplate.find(query, B2BTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(b2BTransactions) ;
    }

    @GetMapping("/b2bTransactionByProduct/{productName}")
    @CircuitBreaker(name = "B2BTransactionFallBack", fallbackMethod = "getB2BTransactionByProductName_FallBack")
    public ResponseEntity<List<B2BTransaction>> getB2BTransactionByProductName(@PathVariable("productName") String productName) {

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
        List<B2BTransaction> b2BTransactions = mongoTemplate.find(query, B2BTransaction.class);

        return ResponseEntity.status(HttpStatus.OK).body(b2BTransactions) ;
    }

    private ResponseEntity<List<B2CTransaction>> getB2BTransactionByProductName_FallBack(String productName, RuntimeException e ) {

        List<B2CTransaction> b2CTransactionList = new ArrayList<>();

        B2CTransaction b2CTransaction = new B2CTransaction();

        b2CTransaction.setName("Fake B2BTransaction - Product Service is not available");

        b2CTransactionList.add(b2CTransaction);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(b2CTransactionList);
    }


}
