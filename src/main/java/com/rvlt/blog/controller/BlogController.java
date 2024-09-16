package com.rvlt.blog.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.Blog;
import com.rvlt.blog.dto.ActionOnBlogsRq;
import com.rvlt.blog.dto.PublishedBlog;
import com.rvlt.blog.dto.SaveDraftDto;
import com.rvlt.blog.service.BlogService;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Get != View
 * Get = User get their own blogs
 * View = User/Other users view PUBLISHED blogs
 */
@RestController
@RequestMapping("/blogs")
public class BlogController {

  @Autowired
  private BlogService blogService;

  @PostMapping("/draft")
  @Operation(summary = "Save as draft")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  public ResponseEntity<ResponseMessage<Void>> saveDraft(@RequestBody RequestMessage<SaveDraftDto> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.saveAsDraft(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @GetMapping("/me")
  @Operation(summary = "Users get all of their blogs.")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  public ResponseEntity<ResponseMessage<Set<Blog>>> getOwnerBlogs(HttpServletRequest httpServletRequest) {
    ResponseMessage<Set<Blog>> res = blogService.getOwnerBlogs(httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @GetMapping("/view/{blogId}")
  @Operation(summary = "User view (read) a blog.")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @Parameter(in = ParameterIn.PATH, name = "blogId", required = true, description = "Blog ID")
  public ResponseEntity<ResponseMessage<PublishedBlog>> viewBlogById(@PathVariable Long blogId, HttpServletRequest httpServletRequest) {
    // Blog must be ADMIN_PUBLISHED
    ResponseMessage<PublishedBlog> res = blogService.viewBlogById(blogId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/submit/{blogId}")
  @Operation(summary = "User submit draft for mod review")
  public ResponseEntity<ResponseMessage<Void>> userSubmitDraftForReview(@PathVariable Long blogId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.userSubmitDraftForReview(blogId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  // ------------------ admin api -------------------- //
  @GetMapping("")
  @Operation(summary = "Admin/Mod view all blogs.")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  public ResponseEntity<ResponseMessage<List<Blog>>> adminGetBlogs(HttpServletRequest httpServletRequest) {
    ResponseMessage<List<Blog>> res = blogService.adminGetBlogs(httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/admin/approve")
  @Operation(summary = "Admin approves(publishes) blogs.")
  public ResponseEntity<ResponseMessage<Void>> adminApproveBlogs(@RequestBody RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.adminApproveBlogs(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/admin/reject")
  @Operation(summary = "Admin rejects blogs.")
  public ResponseEntity<ResponseMessage<Void>> adminRejectBlogs(@RequestBody RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.adminRejectBlogs(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  // ------------------ moderator api -------------------- //
  @PostMapping("/mod/approve")
  @Operation(summary = "Mod approves blogs.")
  public ResponseEntity<ResponseMessage<Void>> modApproveBlogs(@RequestBody RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.modApproveBlogs(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/mod/reject")
  @Operation(summary = "Mod rejects blogs.")
  public ResponseEntity<ResponseMessage<Void>> modRejectBlogs(@RequestBody RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = blogService.modRejectBlogs(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
