package com.tunaforce.product.repository.feign.hub.dto.request;

import java.util.UUID;

public record HubFindInfoRequestDto(
        UUID hubId
) {
}
