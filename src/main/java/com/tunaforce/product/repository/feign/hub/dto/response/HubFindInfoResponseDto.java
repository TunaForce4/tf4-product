package com.tunaforce.product.repository.feign.hub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record HubFindInfoResponseDto(
        @JsonProperty("id")
        UUID hubId,

        String hubName
) {
}
