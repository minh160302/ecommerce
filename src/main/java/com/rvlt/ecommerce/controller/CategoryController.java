package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<ResponseMessage<List<Category>>> getCategories() {
    ResponseMessage<List<Category>> res = categoryService.getCategories();
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** Get all products of a category **/
  @GetMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<List<Product>>> getCategoryById(@PathVariable("categoryId") String categoryId) {
    ResponseMessage<List<Product>> res = categoryService.getProductsByCategory(categoryId);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping
  public ResponseEntity<ResponseMessage<Void>> createCategory(@RequestBody RequestMessage<Category> category) {
    ResponseMessage<Void> res = categoryService.createCategory(category);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** If not passing in `active`, default as false **/
  @PutMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<Void>> updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody RequestMessage<Category> category) {
    ResponseMessage<Void> res = categoryService.updateCategory(categoryId, category);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<Void>> deleteCategory(@PathVariable("categoryId") String categoryId) {
    ResponseMessage<Void> res = categoryService.deleteCategory(categoryId);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  // TODO 2: view counter, most recently viewed, ....
}
