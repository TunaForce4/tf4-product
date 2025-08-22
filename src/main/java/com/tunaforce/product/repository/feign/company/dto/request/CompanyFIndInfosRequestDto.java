package com.tunaforce.product.repository.feign.company.dto.request;

import java.util.List;
import java.util.UUID;

public record CompanyFIndInfosRequestDto(
        List<UUID> companyIdList
) {
}
