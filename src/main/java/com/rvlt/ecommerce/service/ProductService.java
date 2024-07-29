package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;

import java.util.List;

public interface ProductService {
    ResponseMessage<List<Product>> getAllProduct();

    ResponseMessage<Product> getProductById(Long id);

    ResponseMessage<Void> updateProduct(Long id, RequestMessage<UpdateProductRq> request);

  ResponseMessage<List<Category>> getProductCategories(Long productId);
}
