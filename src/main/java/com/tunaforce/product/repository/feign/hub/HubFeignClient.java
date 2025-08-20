package com.tunaforce.product.repository.feign.hub;

import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "hubs",
        url = "localhost:3340",
        path = "/internal/hubs/product-hub",
        fallbackFactory = HubFeignFallbackFactory.class)
public interface HubFeignClient {

    @GetMapping("/find-by-user-id/{userId}")
    HubFindInfoResponseDto findHubInfoByUserId(@PathVariable("userId") UUID userId);

    @GetMapping("/find-by-hub-ids")
    HubFindInfoListResponseDto findHubInfoListByHubIds(@RequestParam List<UUID> hubIds);
}
