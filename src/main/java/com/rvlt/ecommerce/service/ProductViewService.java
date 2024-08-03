package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.model.Product;

public interface ProductViewService {
  void userViewProduct(Product product, Long userId);
}
