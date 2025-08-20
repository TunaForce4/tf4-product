package com.tunaforce.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
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
}
