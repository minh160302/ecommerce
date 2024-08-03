package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  @Modifying
  @Query(value = "WITH ins AS " +
          "(INSERT INTO wishlists (user_id) VALUES (?1) ON CONFLICT (user_id) DO NOTHING RETURNING *)\n" +
          "SELECT * FROM ins " +
          "UNION ALL " +
          "SELECT * FROM wishlists WHERE user_id = ?1 LIMIT 1;", nativeQuery = true)
  List<Wishlist> findWishlistByUserId(Long userId);
}
