package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ProductService {
    ResponseMessage<List<Product>> getAllProduct();

    ResponseMessage<Product> getProductById(Long id, HttpServletRequest httpServletRequest);

    ResponseMessage<Void> updateProduct(Long id, RequestMessage<UpdateProductRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<List<Category>> getProductCategories(Long productId);

  ResponseMessage<List<Product>> getPersonalizedProducts(HttpServletRequest httpServletRequest);
}
