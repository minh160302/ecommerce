package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.repository.CategoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

//  @Autowired
//  private ProductCategoryRepository pcRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public ResponseMessage<List<Category>> getCategories() {
    ResponseMessage<List<Category>> rs = new ResponseMessage<>();
    Status status = new Status();
    List<Category> categories = categoryRepository.findAll();
    rs.setData(categories);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<List<Product>> getProductsByCategory(String categoryId) {
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    Long catId = Long.valueOf(categoryId);
    List<Product> products = productRepository.findProductsInCategory(catId);
    rs.setData(products);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Void> createCategory(RequestMessage<Category> rq) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    status.setHttpStatusCode(HttpStatus.CREATED.value());
    Category category = rq.getData();
    if (category == null || category.getName() == null || category.getDescription() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request: category name or description is empty");
    }
    // default as active = true
    category.setActive(true);
    categoryRepository.save(category);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Void> updateCategory(String categoryId, RequestMessage<Category> rq) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Category input = rq.getData();
    Optional<Category> optCategory = categoryRepository.findById(Long.valueOf(categoryId));
    if (optCategory.isPresent()) {
      Category original = optCategory.get();
      if (input.getName() != null) {
        original.setName(input.getName());
      }
      if (input.getDescription() != null) {
        original.setDescription(input.getDescription());
      }
      original.setActive(input.isActive());
      categoryRepository.save(original);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid request: category not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> deleteCategory(String categoryId) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    categoryRepository.deleteById(Long.valueOf(categoryId));
    rs.setStatus(status);
    return rs;
  }
}
