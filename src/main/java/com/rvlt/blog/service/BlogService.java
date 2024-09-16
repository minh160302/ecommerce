package com.rvlt.blog.service;

import com.rvlt._common.model.Blog;
import com.rvlt.blog.dto.ActionOnBlogsRq;
import com.rvlt.blog.dto.PublishedBlog;
import com.rvlt.blog.dto.SaveDraftDto;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;

public interface BlogService {
  ResponseMessage<Void> saveAsDraft(RequestMessage<SaveDraftDto> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Set<Blog>> getOwnerBlogs(HttpServletRequest httpServletRequest);

  ResponseMessage<List<Blog>> adminGetBlogs(HttpServletRequest httpServletRequest);

  ResponseMessage<PublishedBlog> viewBlogById(Long blogId, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> userSubmitDraftForReview(Long blogId, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> modApproveBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> modRejectBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> adminApproveBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> adminRejectBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest);
}
