package com.rvlt.ecommerce.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrderRf {
    private Long id;
    private String status;
    private Date createdAt;
    private Date submittedAt;
    private String history;
}
