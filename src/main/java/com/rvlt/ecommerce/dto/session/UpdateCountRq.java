package com.rvlt.ecommerce.dto.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCountRq {
    @NonNull
    private Long sessionId;

    @NonNull
    private Long productId;

    private int count;
}
