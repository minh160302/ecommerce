package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ResponseMessage<List<Product>>> getProduct() {
        ResponseMessage<List<Product>> res = productService.getAllProducts();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Product>> getProductById(@PathVariable Long productId){
        ResponseMessage<Product> res = productService.getProductById(productId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
