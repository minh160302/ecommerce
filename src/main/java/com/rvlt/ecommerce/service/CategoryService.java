package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;

import java.util.List;

public interface CategoryService {
  ResponseMessage<List<Category>> getCategories();

  ResponseMessage<List<Product>> getProductsByCategory(String categoryId);

  ResponseMessage<Void> createCategory(RequestMessage<Category> category);

  ResponseMessage<Void> updateCategory(String categoryId, RequestMessage<Category> category);

  ResponseMessage<Void> deleteCategory(String categoryId);
}
