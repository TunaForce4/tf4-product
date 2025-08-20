package com.tunaforce.product.dto.response;

import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Builder
public record ProductFindPageResponseDto(
        long totalElements,

        int totalPages,

        int currentPage,

        int currentSize,

        List<ProductFindDetailResponseDto> data
) {

    public static ProductFindPageResponseDto from(
            Page<ProductDetailsQuerydslResponseDto> page,
            Map<UUID, String> hubs,
            Map<UUID, String> companies
    ) {
        List<ProductFindDetailResponseDto> data = page.getContent().stream()
                .map(product -> ProductFindDetailResponseDto.builder()
                        .productId(product.productId())
                        .hubName(hubs.get(product.hubId()))
                        .companyName(companies.get(product.companyId()))
                        .productName(product.name())
                        .productQuantity(product.quantity())
                        .productPrice(product.price())
                        .createdAt(product.createdAt())
                        .createdAt(product.updatedAt())
                        .build()
                ).toList();

        return ProductFindPageResponseDto.builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .currentSize(page.getNumberOfElements())
                .data(data)
                .build();
    }
}
