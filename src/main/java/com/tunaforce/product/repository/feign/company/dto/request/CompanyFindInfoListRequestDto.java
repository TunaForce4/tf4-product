package com.tunaforce.product.repository.feign.company.dto.request;

import java.util.List;
import java.util.UUID;

public record CompanyFindInfoListRequestDto(
        List<CompanyFindInfoRequestDto> companyIds
) {

    public static CompanyFindInfoListRequestDto from(List<UUID> companyIds) {
        return new CompanyFindInfoListRequestDto(
                companyIds.stream()
                        .map(CompanyFindInfoRequestDto::new)
                        .toList()
        );
    }
}
