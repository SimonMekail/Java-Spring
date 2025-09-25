package com.example.Product_Service.controller;

import com.example.Product_Service.entity.Product;
import com.example.Product_Service.response.ProductResponse;
import com.example.Product_Service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class ProductController {

    @Autowired
    private ProductService productService;



    @GetMapping("/product/{id}")
    @CircuitBreaker(name = "productFallBack", fallbackMethod = "getProductDetails_FallBack")
    public ResponseEntity<Product> getProductDetails(@PathVariable("id") String id) {

        Product product = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/productName/{productName}")
    public ResponseEntity<Product> getProductByNameDetails(@PathVariable("productName") String productName) {

        Product product = productService.getProductByName(productName);

        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @GetMapping("/products")
    public ResponseEntity < List <Product>> getAllProduct(){

        return ResponseEntity.ok().body(productService.getAllProduct());
    }

    @PostMapping("/product")
    public ResponseEntity < Product > createProduct(@RequestBody Product product) {
        return ResponseEntity.ok().body(productService.createProduct(product));
    }

    @PutMapping("/product/{id}")
    public ResponseEntity < Product > updateProduct(@PathVariable String id, @RequestBody Product product) {
        product.setId(id);
        return ResponseEntity.ok().body(productService.updateProduct(product));
    }

    @DeleteMapping("/product/{id}")
    public HttpStatus deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return HttpStatus.OK;
    }


    private ResponseEntity<ProductResponse> getProductDetails_FallBack(String id , RuntimeException e ) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ProductResponse());
    }

}
