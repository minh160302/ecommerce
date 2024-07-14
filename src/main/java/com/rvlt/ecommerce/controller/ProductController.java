package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.AddProductRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-product")
    public ResponseEntity<ResponseMessage<Void>> addProduct(@RequestBody RequestMessage<AddProductRq> request){
        ResponseMessage<Void> res = productService.addProduct(request);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
