package com.tunaforce.product.repository.feign.hub;

import com.tunaforce.product.repository.feign.hub.dto.request.HubFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "hubs",
        path = "/internal/hubs/product-hub",
        fallbackFactory = HubFeignFallbackFactory.class)
public interface HubFeignClient {

    @GetMapping("/find-by-user-id/{userId}")
    HubFindInfoResponseDto findHubInfoByUserId(
            @PathVariable("userId") UUID userId
    );

    @PostMapping("/find-by-hub-ids")
    HubFindInfoListResponseDto findHubInfoListByHubIds(
            @RequestBody HubFindInfoListRequestDto requestDto
    );
}
