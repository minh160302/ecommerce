package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.composite.SessionProduct;
import com.rvlt._common.model.enums.SessionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
  @Column(name = "status", columnDefinition = "status")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private SessionStatus status;

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
