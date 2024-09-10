package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wishlists")
public class Wishlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @JsonIgnore
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
          name = "wishlist_product",
          joinColumns = @JoinColumn(name = "wishlist_id"),
          inverseJoinColumns = @JoinColumn(name = "product_id")
  )
  private Set<Product> products;
}
