package com.tunaforce.product.repository.querydsl.dto.response;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductDetailsQuerydslResponseDto(
        UUID productId,

        UUID hubId,

        UUID companyId,

        String name,

        Integer price,

        Integer quantity,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {

    @QueryProjection
    public ProductDetailsQuerydslResponseDto {
    }
}
