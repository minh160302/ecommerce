package com.rvlt.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "sessions")
public class Session {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "status")
  private String status;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @NotNull
  @Column(name = "total_amount")
  private Double totalAmount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id", nullable=false)
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "session", fetch = FetchType.LAZY)
  Set<SessionProduct> sessionProducts;

  @JsonIgnore
  @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  private Order order;
}
