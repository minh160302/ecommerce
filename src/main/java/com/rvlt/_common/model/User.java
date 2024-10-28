package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "firstname")
  private String firstName;

  @NotNull
  @Column(name = "lastname")
  private String lastName;

  @NotNull
  @Email
  @Column(name = "email", unique = true)
  private String email;

  @NotNull
  @Column(name = "dob")
  private String dob;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private Role role;

  @JsonIgnore
  @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
  private Set<Session> sessions;

  @JsonIgnore
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Wishlist wishlist;

  @JsonIgnore
  @OrderBy("id ASC")
  @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
  private Set<Blog> blogs;

  public User() {
    this.role = Role.user;
    this.sessions = new HashSet<>();
  }

  public boolean checkBlogOwnership(Long blogId) {
    Blog[] blogs = this.getBlogs().toArray(new Blog[0]);
    int left = 0, right = blogs.length - 1;
    while (left <= right) {
      int mid = (left + right) >>> 1;
      if (blogs[mid].getId().equals(blogId)) {
        return true;
      } else if (blogs[mid].getId() > blogId) {
        right = mid - 1;
      } else {
        left = mid + 1;
      }
    }
    blogs = null;
    return false;
  }
}
