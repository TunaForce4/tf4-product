package com.tunaforce.product.repository.feign.company.dto.request;

import java.util.UUID;

public record CompanyFindInfoRequestDto(
        UUID companyId
) {
}
