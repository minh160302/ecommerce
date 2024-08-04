package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query(value = """
          with tmp as
              (select s.id from sessions s inner join users u on s.user_id = u.id where s.status = 'INACTIVE' and u.id = ?2)
          select o.* from tmp t inner join orders o on t.id = o.id where o.id = ?1 limit 1;""",
  nativeQuery = true)
  Optional<Order> findByOrderIdAndUserId(Long id, Long userId);
}
