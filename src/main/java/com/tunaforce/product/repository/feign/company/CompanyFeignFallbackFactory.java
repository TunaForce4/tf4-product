package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyFeignFallbackFactory implements FallbackFactory<CompanyFeignClient> {

    @Override
    public CompanyFeignClient create(Throwable cause) {
        return new CompanyFeignClient() {
            @Override
            public void existsById(UUID companyId) {
                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ProductException.COMPANY_NOT_FOUND);
                }

                throw new CustomRuntimeException(ProductException.COMPANY_SERVICE_UNAVAILABLE);
            }
        };
    }
}
