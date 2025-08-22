package com.tunaforce.product.repository.feign.hub.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record HubFindInfoListResponseDto(
        List<HubFindInfoResponseDto> content
) {

    public Map<UUID, String> toMap() {
        return content.stream()
                .collect(Collectors.toMap(
                        HubFindInfoResponseDto::hubId,
                        HubFindInfoResponseDto::hubName
                ));
    }
}
