package com.rvlt.ecommerce.model.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class SessionProductKey implements Serializable {

  @Column(name = "session_id")
  private Long sessionId;

  @Column(name = "product_id")
  private Long productId;

  // standard constructors, getters, and setters
  // hashcode and equals implementation


  public SessionProductKey(Long sessionId, Long productId) {
    this.sessionId = sessionId;
    this.productId = productId;
  }

  public SessionProductKey() {

  }
}
