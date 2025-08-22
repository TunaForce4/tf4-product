package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.repository.feign.company.dto.request.CompanyFIndInfosRequestDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "company",
        path = "/companies",
        fallbackFactory = CompanyFeignFallbackFactory.class
)
public interface CompanyFeignClient {

    @GetMapping("/users/{userId}")
    CompanyFindInfoResponseDto findCompanyInfoByUserId(
            @PathVariable("userId") UUID userId
    );

    @GetMapping("/{companyId}")
    CompanyFindInfoResponseDto findCompanyInfoByCompanyId(
            @PathVariable("companyId") UUID companyId
    );

    @PostMapping("/list")
    CompanyFindInfoListResponseDto findCompanyInfoListByCompanyIds(
            @RequestBody CompanyFIndInfosRequestDto requestDto
    );
}
