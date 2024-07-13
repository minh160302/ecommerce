package com.rvlt.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  @Column(name = "status")
  private String status;

  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

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
