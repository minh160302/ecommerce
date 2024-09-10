package com.rvlt.ecommerce.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt._common.model.Category;
import com.rvlt._common.model.Product;
import com.rvlt.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Endpoints for managing categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @Operation(summary = "[Admin/Client] Get all categories")
//  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @GetMapping
  public ResponseEntity<ResponseMessage<List<Category>>> getCategories() {
    ResponseMessage<List<Category>> res = categoryService.getCategories();
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** Get all products of a category **/
  @Operation(summary = "[Admin/Client] Get products belong to a category")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @Parameter(in = ParameterIn.PATH, name = "categoryId", required = true, description = "Category ID")
  @GetMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<List<Product>>> getCategoryById(@PathVariable("categoryId") String categoryId) {
    ResponseMessage<List<Product>> res = categoryService.getProductsByCategory(categoryId);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Admin] Create new category")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin ID")
  @PostMapping
  public ResponseEntity<ResponseMessage<Void>> createCategory(@RequestBody RequestMessage<Category> category, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = categoryService.createCategory(category, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** If not passing in `active`, default as false **/
  @Operation(summary = "[Admin] Update existing category")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin ID")
  @Parameter(in = ParameterIn.PATH, name = "categoryId", required = true, description = "Category ID")
  @PutMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<Void>> updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody RequestMessage<Category> category, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = categoryService.updateCategory(categoryId, category, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Admin] Delete existing category")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin ID")
  @Parameter(in = ParameterIn.PATH, name = "categoryId", required = true, description = "Category ID")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<ResponseMessage<Void>> deleteCategory(@PathVariable("categoryId") String categoryId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = categoryService.deleteCategory(categoryId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
