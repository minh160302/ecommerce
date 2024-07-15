package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Product;

import java.util.List;

public interface ProductService {
    ResponseMessage<List<Product>> getAllProduct();

    ResponseMessage<Product> getProductById(Long id);

    ResponseMessage<Void> updateProduct(Long id, UpdateProductRq request);
}
