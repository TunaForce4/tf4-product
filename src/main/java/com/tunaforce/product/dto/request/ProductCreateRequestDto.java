package com.tunaforce.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCreateRequestDto(
        @NotNull(message = "허브 id 값을 입력해주세요.")
        UUID hubId,

        @NotNull(message = "업체 id 값을 입력해주세요.")
        UUID companyId,

        @NotBlank(message = "등록 상품 이름을 입력해주세요.")
        String name,

        @NotNull(message = "등록 상품 수량을 입력해주세요.") @Min(value = 0, message = "등록 상품 수량은 최소 1 이상이어야 합니다.")
        Integer quantity,

        @NotNull(message = "등록 상품 가격을 입력해주세요.") @Min(value = 1000, message = "등록 상품 가격읜 최소 1000원 이상이어야 합니다.")
        Integer price
) {
}
