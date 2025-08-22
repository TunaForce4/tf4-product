package com.tunaforce.product.dto.response;

import java.util.UUID;

public record ProductSimpleReduceStockResponseDto(
        UUID supplyCompanyId,
        Integer price
) {
}
