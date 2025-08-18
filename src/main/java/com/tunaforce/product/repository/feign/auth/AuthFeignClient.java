package com.tunaforce.product.repository.feign.auth;

import com.tunaforce.product.dto.request.AuthCreateProductCheckUserAffiliationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", fallbackFactory = AuthFeignFallbackFactory.class)
public interface AuthFeignClient {

    /**
     * 유저 역할에 따라 상품 생성에 요청된 허브 또는 업체와 로그인한 유저의 허브 또는 업체와의 관계가 유효한지 검증
     */
    @GetMapping("/auth/{userId}/check-affiliation")
    void checkUserAffiliation(
            @RequestBody AuthCreateProductCheckUserAffiliationRequestDto authCreateProductCheckUserAffiliationRequestDto
    );
}
