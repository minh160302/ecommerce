package com.rvlt.ecommerce.dto.order;

import com.rvlt.ecommerce.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderWithSessionProducts {
    private Long id;
    private String status;
    private Date createdAt;
    private Date submittedAt;
    private Double totalAmount;
    private List<ProductInOrder> products;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductInOrder {
        private Long id;
        private String name;
        private int count;
    }
}