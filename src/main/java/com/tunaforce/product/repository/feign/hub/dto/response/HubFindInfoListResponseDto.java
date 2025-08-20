package com.tunaforce.product.repository.feign.hub.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record HubFindInfoListResponseDto(
        @JsonProperty("content")
        List<HubFindInfoResponseDto> data
) {

    public Map<UUID, String> toMap() {
        return data.stream()
                .collect(Collectors.toMap(
                        HubFindInfoResponseDto::hubId,
                        HubFindInfoResponseDto::hubName
                ));
    }
}
