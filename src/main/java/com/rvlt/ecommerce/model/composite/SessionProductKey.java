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
  Long sessionId;

  @Column(name = "product_id")
  Long productId;

  // standard constructors, getters, and setters
  // hashcode and equals implementation
}
