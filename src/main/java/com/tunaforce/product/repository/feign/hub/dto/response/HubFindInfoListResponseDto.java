package com.tunaforce.product.repository.feign.hub.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record HubFindInfoListResponseDto(
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
