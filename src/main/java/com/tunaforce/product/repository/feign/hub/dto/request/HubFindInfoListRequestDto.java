package com.tunaforce.product.repository.feign.hub.dto.request;

import java.util.List;
import java.util.UUID;

public record HubFindInfoListRequestDto(
        List<HubFindInfoRequestDto> hubIds
) {

    public static HubFindInfoListRequestDto from(List<UUID> hubIds) {
        return new HubFindInfoListRequestDto(
                hubIds.stream()
                        .map(HubFindInfoRequestDto::new)
                        .toList()
        );
    }
}
