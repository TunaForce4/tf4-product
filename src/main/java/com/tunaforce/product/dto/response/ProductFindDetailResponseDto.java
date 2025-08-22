package com.tunaforce.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Builder
public record ProductFindDetailResponseDto(
        UUID productId,

        String hubName,

        String companyName,

        String productName,

        Integer productQuantity,

        Integer productPrice,

        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy년 MM월 dd일")
        LocalDateTime updatedAt
) {

    public static ProductFindDetailResponseDto from(
            ProductDetailsQuerydslResponseDto data,
            Map<UUID, String> hubs,
            Map<UUID, String> companies
    ) {
        return ProductFindDetailResponseDto.builder()
                .productId(data.productId())
                .hubName(hubs.get(data.hubId()))
                .companyName(companies.get(data.companyId()))
                .productName(data.name())
                .productQuantity(data.quantity())
                .productPrice(data.price())
                .createdAt(data.createdAt())
                .updatedAt(data.updatedAt())
                .build();
    }
}
