package com.tunaforce.product.service;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.dto.response.ProductFindPageResponseDto;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.entity.UserRole;
import com.tunaforce.product.repository.feign.auth.AuthFeignClient;
import com.tunaforce.product.repository.feign.company.CompanyFeignClient;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponse;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import com.tunaforce.product.repository.feign.hub.HubFeignClient;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import com.tunaforce.product.repository.jpa.ProductJpaRepository;
import com.tunaforce.product.repository.querydsl.ProductQuerydslRepository;
import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final HubFeignClient hubFeignClient;
    private final AuthFeignClient authFeignClient;
    private final CompanyFeignClient companyFeignClient;

    private final ProductJpaRepository productJpaRepository;

    private final ProductQuerydslRepository productQuerydslRepository;

    public void createProduct(ProductCreateRequestDto request, UUID userId) {
        // 유저 역할에 따라 상품 생성에 요청된 허브 또는 업체와 로그인한 유저의 허브 또는 업체와의 관계가 유효한지 검증
//        AuthCreateProductCheckUserAffiliationRequestDto authRequestDto =
//                new AuthCreateProductCheckUserAffiliationRequestDto(
//                        userId,
//                        request.hubId(),
//                        request.companyId()
//                );
//
//        authFeignClient.checkUserAffiliation(authRequestDto);

        // persist a product
        Product product = Product.builder()
                .hubId(request.hubId())
                .companyId(request.companyId())
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();

        productJpaRepository.save(product);
    }

    public ProductFindPageResponseDto findProductPage(
            Pageable pageable,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        Page<ProductDetailsQuerydslResponseDto> page
                = findProductPageByAuthority(pageable, productName, userId, userRole);

        // 조회한 레코드에서 허브와 업체 ID 중복 제거
        Set<UUID> hubSet = page.getContent().stream()
                .map(ProductDetailsQuerydslResponseDto::hubId)
                .collect(Collectors.toSet());

        Set<UUID> companySet = page.getContent().stream()
                .map(ProductDetailsQuerydslResponseDto::companyId)
                .collect(Collectors.toSet());

        // 허브와 업체 정보(이름) 조회
        Map<UUID, String> hubs = getHubs(hubSet);
        Map<UUID, String> companies = getCompanies(companySet);

        return ProductFindPageResponseDto.from(page, hubs, companies);
    }

    /**
     * 권한 별 페이지네이션
     */
    private Page<ProductDetailsQuerydslResponseDto> findProductPageByAuthority(
            Pageable pageable,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        // MASTER - 전체 상품 조회
        if (userRole.equals(UserRole.MASTER)) {
            return productQuerydslRepository.findPage(pageable, null, null, productName);
        }

        // HUB - 특정 허브(로그인 유저) 소속 업체들이 등록한 상품 조회
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto hubInfo = hubFeignClient.findHubInfoByUserId(userId);
            return productQuerydslRepository.findPage(pageable, hubInfo.hubId(), null, productName);
        }

        // COMPANY - 특정 업체(로그인 유저)가 등록한 상품 조회
        if (userRole.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto companyInfo = companyFeignClient.findCompanyInfoByUserId(userId);
            return productQuerydslRepository.findPage(pageable, null, companyInfo.companyId(), productName);
        }

        throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
    }

    /**
     * 허브 이름 조회
     */
    private Map<UUID, String> getHubs(Set<UUID> hubSet) {
        if (hubSet.isEmpty()) {
            return Collections.emptyMap();
        }

        HubFindInfoListResponseDto hubs = hubFeignClient.findHubInfoListByHubIds(new ArrayList<>(hubSet));
        return hubs.toMap();
    }

    /**
     * 업체 이름 조회
     */
    private Map<UUID, String> getCompanies(Set<UUID> companySet) {
        if (companySet.isEmpty()) {
            return Collections.emptyMap();
        }

        CompanyFindInfoListResponse companies = companyFeignClient.findCompanyInfoListByCompanyIds(new ArrayList<>(companySet));
        return companies.toMap();
    }
}
