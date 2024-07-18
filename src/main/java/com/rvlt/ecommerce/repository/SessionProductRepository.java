package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.model.composite.SessionProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionProductRepository extends JpaRepository<SessionProduct, SessionProductKey> {
//  @Override
//  Optional<SessionProduct> findById(SessionProductKey sessionProductKey);

  @Query(value = "select sessions_products.* from sessions_products left join sessions on sessions.id = sessions_products.session_id where sessions.status = 'ACTIVE' and product_id = ?1;",
          nativeQuery = true)
  List<SessionProduct> findActiveByProductId(Long productId);
}
