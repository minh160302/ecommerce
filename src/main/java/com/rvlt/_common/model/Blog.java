package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.enums.BlogStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "blogs")
public class Blog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
  @NotNull
  private String title;

  @Column(name = "slug")
  @NotNull
  private String slug;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Column(name = "view_count")
  @NotNull
  private Integer viewCount;

  @Column(name = "published_content")
  private String content;

  @Column(name = "draft_content")
  private String draft;

  @Column(name = "status", columnDefinition = "status")
  @NotNull
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private BlogStatus status;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", nullable=false)
  private User user;
}
