package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.model.composite.SessionProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionProductRepository extends JpaRepository<SessionProduct, SessionProductKey> {
  @Override
  Optional<SessionProduct> findById(SessionProductKey sessionProductKey);

  List<SessionProduct> findByProductId(Long productId);
}
