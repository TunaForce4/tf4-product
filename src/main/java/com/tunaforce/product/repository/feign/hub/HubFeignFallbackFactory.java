package com.tunaforce.product.repository.feign.hub;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HubFeignFallbackFactory implements FallbackFactory<HubFeignClient> {

    @Override
    public HubFeignClient create(Throwable cause) {
        return new HubFeignClient() {
            @Override
            public void existsByIdWithCompany(UUID hubId, UUID companyId) {
                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ProductException.HUB_NOT_FOUND);
                }

                if (cause instanceof FeignException.BadRequest) {
                    throw new CustomRuntimeException(ProductException.INVALID_HUB_COMPANY_RELATION);
                }

                throw new CustomRuntimeException(ProductException.HUB_SERVICE_UNAVAILABLE);
            }
        };
    }
}
