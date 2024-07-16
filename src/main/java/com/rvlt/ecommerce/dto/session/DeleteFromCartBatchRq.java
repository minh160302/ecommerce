package com.rvlt.ecommerce.dto.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeleteFromCartBatchRq {
    private Long sessionId;
    private List<DeleteFromCartRq> products;
}
