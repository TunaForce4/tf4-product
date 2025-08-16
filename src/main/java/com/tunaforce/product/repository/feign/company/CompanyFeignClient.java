package com.tunaforce.product.repository.feign.company;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service", fallbackFactory = CompanyFeignFallbackFactory.class)
public interface CompanyFeignClient {

    @GetMapping("/companies/{companyId}/exists")
    void existsById(@PathVariable UUID companyId);
}
