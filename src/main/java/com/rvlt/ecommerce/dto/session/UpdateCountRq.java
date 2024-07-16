package com.rvlt.ecommerce.dto.session;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCountRq {
    @NotNull
    private Long sessionId;

    @NotNull
    private Long productId;

    private int count;
}
