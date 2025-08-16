package com.tunaforce.product.dto.request;

import java.util.UUID;

public record AuthCreateProductCheckUserAffiliationRequestDto(
        UUID loginUserId,
        UUID requestHubId,
        UUID requestCompanyId
) {
}
