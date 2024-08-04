package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<ResponseMessage<List<Product>>> getProduct(HttpServletRequest httpServletRequest) {
        ResponseMessage<List<Product>> res = productService.getAllProduct(httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** User view product **/
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Product>> getProductById(@PathVariable Long productId, HttpServletRequest httpServletRequest){
        ResponseMessage<Product> res = productService.getProductById(productId, httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** Update product (only update price at the moment) **/
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Void>> updateProductById(@PathVariable Long productId, @RequestBody RequestMessage<UpdateProductRq> rq, HttpServletRequest httpServletRequest){
        ResponseMessage<Void> res = productService.updateProduct(productId, rq, httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** get categories of a product **/
    @GetMapping("/{productId}/categories")
    public ResponseEntity<ResponseMessage<List<Category>>> getProductCategories(@PathVariable Long productId, HttpServletRequest httpServletRequest){
      ResponseMessage<List<Category>> res = productService.getProductCategories(productId, httpServletRequest);
      return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** Get recommended products for user based on most viewed products **/
    @GetMapping("/personalized/view")
    public ResponseEntity<ResponseMessage<List<Product>>> getPersonalizedProducts(HttpServletRequest httpServletRequest){
      ResponseMessage<List<Product>> res = productService.getPersonalizedProducts(httpServletRequest);
      return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }
}
