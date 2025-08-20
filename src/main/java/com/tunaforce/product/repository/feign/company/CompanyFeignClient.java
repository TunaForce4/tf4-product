package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.repository.feign.company.dto.request.CompanyFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponse;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "companies",
        url = "localhost:3360",
        path = "/internal/companies/product-company",
        fallbackFactory = CompanyFeignFallbackFactory.class)
public interface CompanyFeignClient {

    @GetMapping("/find-by-user-id/{userId}")
    CompanyFindInfoResponseDto findCompanyInfoByUserId(
            @PathVariable("userId") UUID userId
    );

    @GetMapping("/find-by-company-id/{companyId}")
    CompanyFindInfoResponseDto findCompanyInfoByCompanyId(
            @PathVariable("companyId") UUID companyId
    );

    @PostMapping("/find-by-company-ids")
    CompanyFindInfoListResponse findCompanyInfoListByCompanyIds(
            @RequestBody CompanyFindInfoListRequestDto requestDto
    );
}
