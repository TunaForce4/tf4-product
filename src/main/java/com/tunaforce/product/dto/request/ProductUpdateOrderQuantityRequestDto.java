package com.tunaforce.product.dto.request;

public record ProductUpdateOrderQuantityRequestDto(
        Integer originalQuantity,
        Integer updateQuantity
) {
}
