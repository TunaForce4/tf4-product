package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponse;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
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
            public CompanyFindInfoListResponse findCompanyByCompanyIds(List<UUID> companyIds) {
                // TODO throw exceptions
                return null;
            }
        };
    }
}
