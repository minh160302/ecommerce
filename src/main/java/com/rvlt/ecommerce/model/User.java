package com.rvlt.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

  @JsonIgnore
  @OneToMany(mappedBy="user", fetch = FetchType.LAZY)
  private Set<Session> sessions;

  @JsonIgnore
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Wishlist wishlist;

}
