package com.tunaforce.product.repository.feign.company;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import com.tunaforce.product.repository.feign.company.dto.request.CompanyFIndInfosRequestDto;
import com.tunaforce.product.repository.feign.company.dto.request.CompanyFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CompanyFeignFallbackFactory implements FallbackFactory<CompanyFeignClient> {

    @Override
    public CompanyFeignClient create(Throwable cause) {
        return new CompanyFeignClient() {

            @Override
            public CompanyFindInfoResponseDto findCompanyInfoByUserId(UUID userId) {
                log.error("CompanyFeignClientFallbackFactory");
                if (cause instanceof FeignException.NotFound) {
                    throw new CustomRuntimeException(ProductException.COMPANY_NOT_FOUND);
                }

                throw new CustomRuntimeException(ProductException.COMPANY_SERVICE_UNAVAILABLE);
            }

            @Override
            public CompanyFindInfoResponseDto findCompanyInfoByCompanyId(UUID companyId) {
                log.error("find company info by company hubId");
                throw new CustomRuntimeException(ProductException.COMPANY_SERVICE_UNAVAILABLE);
            }

            @Override
            public CompanyFindInfoListResponseDto findCompanyInfoListByCompanyIds(CompanyFIndInfosRequestDto requestDto) {
                log.error("find company info list by company ids");
                throw new CustomRuntimeException(ProductException.COMPANY_SERVICE_UNAVAILABLE);
            }
        };
    }
}
