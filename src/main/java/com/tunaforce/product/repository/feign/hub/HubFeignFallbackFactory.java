package com.tunaforce.product.repository.feign.hub;

import com.tunaforce.product.repository.feign.hub.dto.request.HubFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class HubFeignFallbackFactory implements FallbackFactory<HubFeignClient> {

    @Override
    public HubFeignClient create(Throwable cause) {
        return new HubFeignClient() {

            @Override
            public HubFindInfoResponseDto findHubInfoByUserId(UUID userId) {
                // TODO throw exceptions
                return null;
            }

            @Override
            public HubFindInfoListResponseDto findHubInfoListByHubIds(HubFindInfoListRequestDto requestDto) {
                return null;
            }

            @Override
            public HubFindInfoListResponseDto findHubInfoAll(int page, int size) {
                return null;
            }
        };
    }
}
