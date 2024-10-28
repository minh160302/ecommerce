package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
  @Id
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "status", columnDefinition = "status")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private OrderStatus status;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "submitted_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date submitted_at;

  @NotNull
  @Column(name = "history")
  private String history;

  // whoever owns the foreign key column gets the @JoinColumn annotation.
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  @JsonIgnore
  private Session session;
}
