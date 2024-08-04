package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.composite.ProductView;
import com.rvlt.ecommerce.model.composite.ProductViewKey;
import com.rvlt.ecommerce.repository.ProductViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProductViewServiceImpl implements ProductViewService {
  @Autowired
  private ProductViewRepository pvRepository;

  @Override
  public void userViewProduct(Product product, Long userId) {
    ProductViewKey key = new ProductViewKey(userId, product.getId());
    Optional<ProductView> optProductView = pvRepository.findById(key);
    ProductView productView = optProductView.isPresent() ? optProductView.get() : new ProductView(key);
    long newCount = productView.getCount() + 1;
    String history = productView.getHistory();
    if (!history.isEmpty()) {
      String[] arr = history.split("_");
      if (arr.length == 3) {
        arr[0] = arr[1];
        arr[1] = arr[2];
        arr[2] = String.valueOf(new Date());
        history = String.join("_", arr);
      }
      else {
        history += "_" + new Date();
      }
    }
    else {
      history = String.valueOf(new Date());
    }
    productView.setHistory(history);
    productView.setCount(newCount);
    pvRepository.save(productView);
  }
}
