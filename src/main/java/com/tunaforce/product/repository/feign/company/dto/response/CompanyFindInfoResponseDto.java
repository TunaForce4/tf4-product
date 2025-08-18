package com.tunaforce.product.repository.feign.company.dto.response;

import java.util.UUID;

public record CompanyFindInfoResponseDto(
        UUID companyId,

        String companyName
) {
}
