package com.example.Company_Service.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;

@Document(collection = "b2bTransactions")
public class B2BTransaction  {
    @Id
    private String id;
    private String name;

    @DBRef
    private Company sellerCompany;

    @DBRef
    private Company buyerCompany;

    @DBRef
    private Product product;

    private double quantity;
    @CreatedDate
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Company getSellerCompany() {
        return sellerCompany;
    }

    public void setSellerCompany(Company sellerCompany) {
        this.sellerCompany = sellerCompany;
    }

    public Company getBuyerCompany() {
        return buyerCompany;
    }

    public void setBuyerCompany(Company buyerCompany) {
        this.buyerCompany = buyerCompany;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


