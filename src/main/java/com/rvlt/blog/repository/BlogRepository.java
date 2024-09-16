package com.rvlt.blog.repository;

import com.rvlt._common.model.Blog;
import com.rvlt.blog.dto.PublishedBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
//  @Query(value = "select id, user_id, title, slug, published_content, created_at, updated_at, status, view_count from blogs",
//          nativeQuery = true)
//  Optional<PublishedBlog> viewPublishedBlog(Long id);
}
