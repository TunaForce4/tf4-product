package com.tunaforce.product.repository.feign.auth;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class AuthFeignFallbackFactory implements FallbackFactory<AuthFeignClient> {

    @Override
    public AuthFeignClient create(Throwable cause) {
        return new AuthFeignClient() {
            @Override
            public void existsById(UUID userId) {
                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ProductException.USER_NOT_FOUND);
                }

                throw new CustomRuntimeException(ProductException.AUTH_SERVICE_UNAVAILABLE);
            }
        };
    }
}
