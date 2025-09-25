package com.example.Company_Service.service;

import com.example.Company_Service.entity.B2BTransaction;
import com.example.Company_Service.entity.Company;
import com.example.Company_Service.entity.Customer;
import com.example.Company_Service.repository.B2BTransactionRepo;
import com.example.Company_Service.repository.CompanyRepo;
import com.example.Company_Service.response.CompanyResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    public Company getCompanyById(String id) {
        Optional<Company> company = companyRepo.findById(id);
        Company companyResponse = mapper.map(company, Company.class);
        return companyResponse;
    }

    public Company getCompanyByName(String companyName) {

        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(companyName));

        Company company = mongoTemplate.findOne(query, Company.class);
        Company companyResponse = mapper.map(company, Company.class);
        return companyResponse;
    }

    public List < Company > getAllCompanies() {
        return companyRepo.findAll();
    }


    public Company createCompany(Company company) {
        company.setId(null);
        return companyRepo.save(company);
    }

    public Company updateCompany(Company company) {
        Optional < Company > companyDb = companyRepo.findById(company.getId());

        if (companyDb.isPresent()) {
            Company companyUpdate = companyDb.get();
            companyUpdate.setId(company.getId());
            companyUpdate.setName(company.getName());
            companyUpdate.setAddress(company.getAddress());
            companyUpdate.setDescription(company.getDescription());

            companyRepo.save(companyUpdate);
            return companyUpdate;
        } else {
            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + company.getId());
        }
    }

    public void deleteCompany(String companyId) {
        Optional < Company > companyDb = companyRepo.findById(companyId);

        if (companyDb.isPresent()) {
            companyRepo.delete(companyDb.get());
        }
        else {

            throw new com.example.demo.exception.ResourceNotFoundException("Record not found with id : " + companyId);
        }

    }



}
