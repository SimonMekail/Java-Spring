package com.example.Company_Service.controller;

import com.example.Company_Service.entity.B2CTransaction;
import com.example.Company_Service.entity.Company;
import com.example.Company_Service.entity.Customer;
import com.example.Company_Service.response.CompanyResponse;
import com.example.Company_Service.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;



    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyDetails(@PathVariable("id") String id) {

        Company product = companyService.getCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/companyName/{companyName}")
    public ResponseEntity<Company> getCompanyByNameDetails(@PathVariable("companyName") String companyName) {

        Company company = companyService.getCompanyByName(companyName);

        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

//    @GetMapping("/company/{id}")
//    @CircuitBreaker(name = "companyFallBack", fallbackMethod = "getCompanyDetails_FallBack")
//    public ResponseEntity<Customer> getCompanyDetails(@PathVariable("id") String id) {
//
//        // Get ServiceInstance list using serviceId
//        ServiceInstance serviceInstance = loadBalancerClient.choose("customer-service");
//
//        // Read URI and Add path that returns url
//        String uri = serviceInstance.getUri().toString();
//
//        // Make HTTP call and get Response data
//        Customer customer = restTemplate.getForObject(uri + "/customerService/api/customer/"+id, Customer.class);
//
//
//        return ResponseEntity.ok().body(customer);
//    }

    @GetMapping("/test")
    @CircuitBreaker(name = "loadBalanceFallBack", fallbackMethod = "loadBalance_FallBack")
    public String testLoadBalancing() {
        ServiceInstance instance = loadBalancerClient.choose("customer-service");
        String instanceId = instance.getInstanceId();
        return "Request served by instance: " + instanceId + " port : " + instance.getPort();
    }

    private String loadBalance_FallBack(RuntimeException e ) {

        return "customer service is not available";
    }

    @GetMapping("/companies")
    public ResponseEntity < List <Company>> getAllCompanies(){

        return ResponseEntity.ok().body(companyService.getAllCompanies());
    }

    private ResponseEntity<CompanyResponse> getCompanyDetails_FallBack(String id , RuntimeException e ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CompanyResponse());
    }

    @PostMapping("/company")
    public ResponseEntity < Company > createCompany(@RequestBody Company company) {
        return ResponseEntity.ok().body(companyService.createCompany(company));
    }

    @PutMapping("/company/{id}")
    public ResponseEntity < Company > updateCompany(@PathVariable String id, @RequestBody Company company) {
        company.setId(id);
        return ResponseEntity.ok().body(companyService.updateCompany(company));
    }

    @DeleteMapping("/company/{id}")
    public HttpStatus deleteCompany(@PathVariable String id) {
        companyService.deleteCompany(id);
        return HttpStatus.OK;
    }

}
