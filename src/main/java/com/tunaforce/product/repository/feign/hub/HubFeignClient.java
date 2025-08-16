package com.tunaforce.product.repository.feign.hub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service", fallbackFactory = HubFeignFallbackFactory.class)
public interface HubFeignClient {
}
