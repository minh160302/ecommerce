package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.ProductRf;

import java.util.List;

public interface ProductService {
    ResponseMessage<List<ProductRf>> getAllProduct();

    ResponseMessage<ProductRf> getProductById(Long id);

//    ResponseMessage<Void> addProduct(RequestMessage<AddProductRq> request);
}
