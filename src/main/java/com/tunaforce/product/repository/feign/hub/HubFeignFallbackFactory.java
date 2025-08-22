package com.tunaforce.product.repository.feign.hub;

import com.tunaforce.product.repository.feign.hub.dto.request.HubFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class HubFeignFallbackFactory implements FallbackFactory<HubFeignClient> {

    @Override
    public HubFeignClient create(Throwable cause) {
        return new HubFeignClient() {

            @Override
            public HubFindInfoResponseDto findHubInfoByUserId(UUID userId) {
                log.error("find hub info by user hubId");
                // TODO throw exceptions
                return null;
            }

            @Override
            public List<HubFindInfoResponseDto> findHubInfoAll(int page, int size) {
                log.error("find hub info all");
                return null;
            }
        };
    }
}
