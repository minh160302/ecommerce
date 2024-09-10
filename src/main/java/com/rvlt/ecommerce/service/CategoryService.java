package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt._common.model.Category;
import com.rvlt._common.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface CategoryService {
  ResponseMessage<List<Category>> getCategories();

  ResponseMessage<List<Product>> getProductsByCategory(String categoryId);

  ResponseMessage<Void> createCategory(RequestMessage<Category> category, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> updateCategory(String categoryId, RequestMessage<Category> category, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> deleteCategory(String categoryId, HttpServletRequest httpServletRequest);
}
