package com.tunaforce.product.dto.response;

import java.util.List;

public record ProductSimpleListResponseDto(
        List<ProductSimpleResponseDto> data
) {

}
