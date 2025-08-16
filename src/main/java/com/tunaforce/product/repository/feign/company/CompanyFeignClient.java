package com.tunaforce.product.repository.feign.company;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "company-service", fallbackFactory = CompanyFeignFallbackFactory.class)
public interface CompanyFeignClient {
}
