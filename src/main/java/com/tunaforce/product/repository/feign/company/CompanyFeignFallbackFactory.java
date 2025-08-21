package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.repository.feign.company.dto.request.CompanyFIndInfosRequestDto;
import com.tunaforce.product.repository.feign.company.dto.request.CompanyFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyFeignFallbackFactory implements FallbackFactory<CompanyFeignClient> {

    @Override
    public CompanyFeignClient create(Throwable cause) {
        return new CompanyFeignClient() {

            @Override
            public CompanyFindInfoResponseDto findCompanyInfoByUserId(UUID userId) {
                // TODO throw exceptions
                return null;
            }

            @Override
            public CompanyFindInfoResponseDto findCompanyInfoByCompanyId(UUID companyId) {
                return null;
            }

            @Override
            public CompanyFindInfoListResponseDto findCompanyInfoListByCompanyIds(CompanyFIndInfosRequestDto requestDto) {
                return null;
            }
        };
    }
}
