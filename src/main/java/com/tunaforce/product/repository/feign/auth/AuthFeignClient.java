package com.tunaforce.product.repository.feign.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service", fallbackFactory = AuthFeignFallbackFactory.class)
public interface AuthFeignClient {

    @GetMapping("/auth/{userId}/exists")
    void existsById(@PathVariable UUID userId);
}
