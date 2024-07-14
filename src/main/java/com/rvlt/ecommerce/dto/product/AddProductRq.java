package com.rvlt.ecommerce.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddProductRq {
    private Long id;
    private String name;
    private int inStock;
    private double price;
}
