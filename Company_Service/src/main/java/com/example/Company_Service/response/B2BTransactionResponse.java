package com.example.Company_Service.response;

import com.example.Company_Service.entity.Company;
import com.example.Company_Service.entity.Product;



public class B2BTransactionResponse {

    private String id;

    private Company sellerCompany;

    private Company buyerCompany;

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
    private Product product;

    private double quantity;



}




