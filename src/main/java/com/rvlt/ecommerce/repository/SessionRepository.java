package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
  @Query(value = "select * from sessions where id = ?1 and user_id = ?2;", nativeQuery = true)
  Optional<Session> findBySessionIdAndUserId(Long sessionId, Long userId);


  @Query(value = "SELECT * from sessions where status = 'ACTIVE' and user_id = ?1",
          nativeQuery = true)
  List<Session> findActiveSessionByUser(Long userId);
}
