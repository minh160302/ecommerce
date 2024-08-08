package com.rvlt.ecommerce.dto.order;

import com.rvlt.ecommerce.model.Order;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Session;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PreviousOrderRs {
    private List<OrderWithSessionProducts> previousOrders;
}


