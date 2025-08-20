package com.tunaforce.product.repository.feign.company.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record CompanyFindInfoListResponse(
        List<CompanyFindInfoResponseDto> data
) {

    public Map<UUID, String> toMap() {
        return data.stream()
                .collect(Collectors.toMap(
                        CompanyFindInfoResponseDto::companyId,
                        CompanyFindInfoResponseDto::companyName
                ));
    }
}
