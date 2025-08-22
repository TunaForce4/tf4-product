package com.tunaforce.product.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.util.UUID;

public record ProductSimpleResponseDto(
        UUID productId,
        UUID companyId,
        UUID hubId,
        String productName
) {

    @QueryProjection
    public ProductSimpleResponseDto {
    }
}
