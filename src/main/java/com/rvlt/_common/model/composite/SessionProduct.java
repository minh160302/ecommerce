package com.rvlt._common.model.composite;

import com.rvlt._common.model.Product;
import com.rvlt._common.model.Session;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sessions_products")
public class SessionProduct {
  @EmbeddedId
  private SessionProductKey id;

  @ManyToOne
  @MapsId("sessionId")
  @JoinColumn(name = "session_id")
  private Session session;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "count")
  private int count;

  @Override
  public String toString() {
    return "SessionProduct{" +
            "id=" + id +
            ", session=" + session +
            ", product=" + product +
            ", count=" + count +
            '}';
  }
}
