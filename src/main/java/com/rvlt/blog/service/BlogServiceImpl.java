package com.rvlt.blog.service;

import com.rvlt._common.model.Blog;
import com.rvlt._common.model.User;
import com.rvlt._common.model.enums.BlogStatus;
import com.rvlt._common.model.enums.Role;
import com.rvlt.blog.dto.ActionOnBlogsRq;
import com.rvlt.blog.dto.PublishedBlog;
import com.rvlt.blog.dto.SaveDraftDto;
import com.rvlt.blog.repository.BlogRepository;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

  @Autowired
  private Validator validator;

  @Autowired
  private BlogRepository blogRepository;

  @Override
  public ResponseMessage<Void> saveAsDraft(RequestMessage<SaveDraftDto> request, HttpServletRequest httpServletRequest) {
    User user = validator.getCurrentUser(httpServletRequest);
    SaveDraftDto input = request.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    if (input.getContent() == null || input.getTitle() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid save draft request.");
    }
    Date currentTime = new Date();
    Blog blog;
    if (input.getId() == null || input.getId().isEmpty()) {
      // create new blog
      blog = new Blog();
      blog.setTitle(input.getTitle());
      blog.setDraft(input.getContent());
      blog.setSlug(UUID.randomUUID().toString());
      blog.setCreatedAt(currentTime);
      blog.setUpdatedAt(currentTime);
      blog.setViewCount(0);
      blog.setStatus(BlogStatus.UNPUBLISHED);
      blog.setUser(user);
    } else {
      // update blog
      Long blogId = Long.parseLong(input.getId());
      Optional<Blog> optBlog = blogRepository.findById(blogId);
      if (optBlog.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found.");
      }
      if (!user.checkBlogOwnership(blogId)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this blog");
      }
      blog = optBlog.get();
      if (blog.getStatus() != BlogStatus.UNPUBLISHED)
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid blog status: " + blog.getStatus());
      blog.setDraft(input.getContent());
      blog.setTitle(input.getTitle());
      blog.setUpdatedAt(currentTime);
    }
    blogRepository.save(blog);
    rs.setStatus(new Status());
    return rs;
  }

  @Override
  public ResponseMessage<Set<Blog>> getOwnerBlogs(HttpServletRequest httpServletRequest) {
    User user = validator.getCurrentUser(httpServletRequest);
    ResponseMessage<Set<Blog>> rs = new ResponseMessage<>();
    rs.setData(user.getBlogs());
    rs.setStatus(new Status());
    return rs;
  }

  @Override
  public ResponseMessage<PublishedBlog> viewBlogById(Long blogId, HttpServletRequest httpServletRequest) {
    User user = validator.getCurrentUser(httpServletRequest);
    ResponseMessage<PublishedBlog> rs = new ResponseMessage<>();
    Optional<Blog> optBlog = blogRepository.findById(blogId);
    if (optBlog.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found.");
    Blog blog = optBlog.get();
    if (blog.getStatus() != BlogStatus.ADMIN_PUBLISHED)
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Blog is not yet available to access.");
    // user view their other users' blogs -> update viewCount
    if (!user.checkBlogOwnership(blogId)) {
      blog.setViewCount(blog.getViewCount() + 1);
    }
    blogRepository.save(blog);
    rs.setStatus(new Status());
    rs.setData(new PublishedBlog(blog));
    return rs;
  }

  // TODO: moderators & admins create/update blogs then submit -> different handlers?
  @Override
  public ResponseMessage<Void> userSubmitDraftForReview(Long blogId, HttpServletRequest httpServletRequest) {
    User user = validator.getCurrentUser(httpServletRequest);
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Optional<Blog> optBlog = blogRepository.findById(blogId);
    if (optBlog.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found.");
    if (!user.checkBlogOwnership(blogId))
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this blog");
    Blog blog = optBlog.get();
    BlogStatus blogStatus = blog.getStatus();
    if (blogStatus == BlogStatus.UNPUBLISHED) {
      blog.setUpdatedAt(new Date());
      blog.setStatus(BlogStatus.WAITING_MOD_APPROVAL);
    } else
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid blog status.");
    blogRepository.save(blog);
    rs.setStatus(new Status());
    return rs;
  }

  @Override
  public ResponseMessage<Void> modApproveBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    return this.modHandleBlogAction(BlogStatus.MOD_APPROVED, request.getData(), httpServletRequest);
  }

  @Override
  public ResponseMessage<Void> modRejectBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    return this.modHandleBlogAction(BlogStatus.MOD_REJECTED, request.getData(), httpServletRequest);
  }

  // --------------------- admin api --------------------- //
  @Override
  public ResponseMessage<List<Blog>> adminGetBlogs(HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    List<Blog> blogs = blogRepository.findAll();
    ResponseMessage<List<Blog>> rs = new ResponseMessage<>();
    rs.setData(blogs);
    rs.setStatus(new Status());
    return rs;
  }

  @Override
  public ResponseMessage<Void> adminApproveBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    return this.adminHandleBlogAction(BlogStatus.ADMIN_PUBLISHED, request.getData(), httpServletRequest);
  }

  @Override
  public ResponseMessage<Void> adminRejectBlogs(RequestMessage<ActionOnBlogsRq> request, HttpServletRequest httpServletRequest) {
    return this.adminHandleBlogAction(BlogStatus.ADMIN_REJECTED, request.getData(), httpServletRequest);
  }

  // --------------------- private helper methods --------------------- //
  private ResponseMessage<Void> adminHandleBlogAction(BlogStatus targetStatus, ActionOnBlogsRq request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    validator.validateAdmin(httpServletRequest);
    StringBuilder sb_FailedMsg = new StringBuilder();
    for (Long blogId : request.getBlogs()) {
      Optional<Blog> optBlog = blogRepository.findById(blogId);
      if (optBlog.isEmpty())
        sb_FailedMsg.append("Not Found: ").append(blogId).append("\n");
      else {
        Blog blog = optBlog.get();
        BlogStatus currentStatus = blog.getStatus();
        if (currentStatus != BlogStatus.MOD_APPROVED) {
          sb_FailedMsg.append("Invalid status: ").append(currentStatus).append(" ").append(blogId).append("\n");
          continue;
        }
        blog.setStatus(targetStatus);
        blog.setContent(blog.getDraft());
        blogRepository.save(blog);
      }
    }
    Status status = new Status();
    if (!sb_FailedMsg.isEmpty()) {
      status.setMessage("Admin failed to perform action on the following blogs:" + sb_FailedMsg);
    }
    rs.setStatus(status);
    return rs;
  }

  private ResponseMessage<Void> modHandleBlogAction(BlogStatus targetStatus, ActionOnBlogsRq request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    validator.validateAdmin(httpServletRequest);
    StringBuilder sb_FailedMsg = new StringBuilder();
    for (Long blogId : request.getBlogs()) {
      Optional<Blog> optBlog = blogRepository.findById(blogId);
      if (optBlog.isEmpty())
        sb_FailedMsg.append("Not Found: ").append(blogId).append("\n");
      else {
        Blog blog = optBlog.get();
        BlogStatus currentStatus = blog.getStatus();
        if (currentStatus != BlogStatus.WAITING_MOD_APPROVAL && currentStatus != BlogStatus.MOD_APPROVED && currentStatus != BlogStatus.MOD_REJECTED) {
          sb_FailedMsg.append("Invalid status: ").append(blogId).append("\n");
          continue;
        }
        blog.setStatus(targetStatus);
        blog.setUpdatedAt(new Date());
        blogRepository.save(blog);
      }
    }
    Status status = new Status();
    if (!sb_FailedMsg.isEmpty()) {
      status.setMessage("Mod failed to perform action on the following blogs:" + sb_FailedMsg);
    }
    rs.setStatus(status);
    return rs;
  }
}
