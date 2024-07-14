package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.AddProductRq;
import com.rvlt.ecommerce.model.Product;

import java.util.List;

public interface ProductService {
    ResponseMessage<List<Product>> getAllProducts();

    ResponseMessage<Product> getProductById(Long id);

    ResponseMessage<Void> addProduct(RequestMessage<AddProductRq> request);
}
