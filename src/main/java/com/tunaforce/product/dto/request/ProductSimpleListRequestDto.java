package com.tunaforce.product.dto.request;

import java.util.List;

public record ProductSimpleListRequestDto(
        List<ProductSimpleRequestDto> productIds
) {
}
