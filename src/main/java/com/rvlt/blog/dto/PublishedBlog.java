package com.rvlt.blog.dto;

import com.rvlt._common.model.Blog;
import com.rvlt._common.model.enums.BlogStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublishedBlog {
  private Long id;
  private String title;
  private String slug;
  private Date createdAt;
  private Date updatedAt;
  private Integer viewCount;
  private String content;
  private BlogStatus status;

  public PublishedBlog(Blog blog) {
    this.id = blog.getId();
    this.title = blog.getTitle();
    this.slug = blog.getSlug();
    this.createdAt = blog.getCreatedAt();
    this.updatedAt = blog.getUpdatedAt();
    this.viewCount = blog.getViewCount();
    this.content = blog.getContent();
    this.status = blog.getStatus();
  }
}
