package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  @Query(value = "SELECT * FROM wishlists WHERE user_id = ?1 LIMIT 1;", nativeQuery = true)
  Optional<Wishlist> findWishlistByUserId(Long userId);
}
