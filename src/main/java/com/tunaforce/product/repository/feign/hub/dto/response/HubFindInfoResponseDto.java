package com.tunaforce.product.repository.feign.hub.dto.response;

import java.util.UUID;

public record HubFindInfoResponseDto(
        UUID hubId,

        String hubName
) {
}
