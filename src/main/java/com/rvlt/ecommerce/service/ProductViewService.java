package com.rvlt.ecommerce.service;

import com.rvlt._common.model.Product;

public interface ProductViewService {
  void userViewProduct(Product product, Long userId);
}
