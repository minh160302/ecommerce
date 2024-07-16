package com.rvlt.ecommerce.dto.session;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeleteFromCartRq {
    @NotNull
    private Long productId;
    @NotNull
    private Long sessionId;
}
