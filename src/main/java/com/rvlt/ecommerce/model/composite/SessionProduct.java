package com.rvlt.ecommerce.model.composite;

import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sessions_products")
public class SessionProduct {
  @EmbeddedId
  SessionProductKey id;

  @ManyToOne
  @MapsId("sessionId")
  @JoinColumn(name = "session_id")
  Session session;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  Product product;

  @Column(name = "count")
  int count;
}
