package com.rvlt.ecommerce.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt._common.model.Category;
import com.rvlt._common.model.Product;
import com.rvlt.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "[Admin/Client] Get all products")
    @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
    @GetMapping("")
    public ResponseEntity<ResponseMessage<List<Product>>> getProduct(HttpServletRequest httpServletRequest) {
        ResponseMessage<List<Product>> res = productService.getAllProduct(httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** User view product **/
    @Operation(summary = "[Admin/Client] View product details")
    @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
    @Parameter(in = ParameterIn.PATH, name = "productId", required = true, description = "Product ID")
    @GetMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Product>> getProductById(@PathVariable Long productId, HttpServletRequest httpServletRequest){
        ResponseMessage<Product> res = productService.getProductById(productId, httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** Update product (only update price at the moment) **/
    @Operation(summary = "[Admin] Update product details")
    @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID must be admin's value")
    @Parameter(in = ParameterIn.PATH, name = "productId", required = true, description = "Product ID")
    @PutMapping("/{productId}")
    public ResponseEntity<ResponseMessage<Void>> updateProductById(@PathVariable Long productId, @RequestBody RequestMessage<UpdateProductRq> rq, HttpServletRequest httpServletRequest){
        ResponseMessage<Void> res = productService.updateProduct(productId, rq, httpServletRequest);
        return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** get categories of a product **/
    @Operation(summary = "[Admin/Client] Get categories of a product")
    @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
    @Parameter(in = ParameterIn.PATH, name = "productId", required = true, description = "Product ID")
    @GetMapping("/{productId}/categories")
    public ResponseEntity<ResponseMessage<List<Category>>> getProductCategories(@PathVariable Long productId, HttpServletRequest httpServletRequest){
      ResponseMessage<List<Category>> res = productService.getProductCategories(productId, httpServletRequest);
      return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }

    /** Get recommended products for user based on most viewed products **/
    @Operation(summary = "[Client] Get an user's personalized list of products")
    @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
    @GetMapping("/personalized/view")
    public ResponseEntity<ResponseMessage<List<Product>>> getPersonalizedProducts(HttpServletRequest httpServletRequest){
      ResponseMessage<List<Product>> res = productService.getPersonalizedProducts(httpServletRequest);
      return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
    }
}
