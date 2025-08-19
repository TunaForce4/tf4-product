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

    /**
     * 주문 용 전체 상품 페이지네이션
     */
    public ProductFindPageResponseDto findProductPageForOrder(
            Pageable pageable,
            String productName
    ) {
        Page<ProductDetailsQuerydslResponseDto> page
                = productQuerydslRepository.findPage(pageable, null, null, productName);

        return mapPageToResponse(page);
    }

    /**
     * 허브 소속 업체들이 등록한 상품 페이지네이션
     */
    public ProductFindPageResponseDto findProductPageByHub(
            Pageable pageable,
            UUID hubId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        Page<ProductDetailsQuerydslResponseDto> page
                = findHubProductPageByAuthority(pageable, hubId, productName, userId, userRole);

        return mapPageToResponse(page);
    }

    /**
     * 허브 소속 업체들이 등록한 상품 페이지네이션
     */
    public ProductFindPageResponseDto findProductPageByCompany(
            Pageable pageable,
            UUID companyId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        Page<ProductDetailsQuerydslResponseDto> page
                = findCompanyProductPageByAuthority(pageable, companyId, productName, userId, userRole);

        return mapPageToResponse(page);
    }

    /**
     * 특정 허브 소속 업체들의 등록 상품에 대한 권한별 조회
     */
    private Page<ProductDetailsQuerydslResponseDto> findHubProductPageByAuthority(
            Pageable pageable,
            UUID hubId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        // 업체는 자신의 업체만 조회 가능
        if (userRole.equals(UserRole.COMPANY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        // 로그인한 유저가 허브 담당자 일 때 요청한 허브에 접근 가능한지 확인
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto hubInfo = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(hubId, hubInfo.hubId());
        }

        return productQuerydslRepository.findPage(pageable, hubId, null, productName);
    }

    /**
     * 특정 업체의 등록 상품에 대한 권한별 조회
     */
    private Page<ProductDetailsQuerydslResponseDto> findCompanyProductPageByAuthority(
            Pageable pageable,
            UUID companyId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        // 로그인한 유저가 허브 담당자 일 때 요청한 업체가 소속 업체인지 확인
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto hubInfo = hubFeignClient.findHubInfoByUserId(userId);
            CompanyFindInfoListResponse companyInfos
                    = companyFeignClient.findCompanyInfoListByCompanyIds(List.of(companyId));

            CompanyFindInfoResponseDto companyInfo = companyInfos.data().stream().findFirst()
                    .orElseThrow(() -> new CustomRuntimeException(ProductException.COMPANY_NOT_FOUND));

            // 로그인한 유저의 허브가 요청한 업체의 담당 허브인지 확인
            validateUuidMatch(hubInfo.hubId(), companyInfo.hubId());
        }

        // 로그인한 유저가 업체 담당자 일 때 자신의 업체인지 확인
        if (userRole.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto companyInfo = companyFeignClient.findCompanyInfoByUserId(userId);
            validateUuidMatch(companyId, companyInfo.companyId());
        }

        return productQuerydslRepository.findPage(pageable, null, companyId, productName);
    }

    public void validateUuidMatch(UUID expectedId, UUID actualId) {
        if (!expectedId.equals(actualId)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }
    }

    public ProductFindPageResponseDto mapPageToResponse(Page<ProductDetailsQuerydslResponseDto> page) {
        // 조회한 레코드에서 허브와 업체 ID 중복 제거
        Set<UUID> hubSet = getUniqueHubIds(page);
        Set<UUID> companySet = getUniqueCompanyIds(page);

        // 허브와 업체 정보(이름) 조회
        Map<UUID, String> hubs = getHubs(hubSet);
        Map<UUID, String> companies = getCompanies(companySet);

        return ProductFindPageResponseDto.from(page, hubs, companies);
    }

    /**
     * 조회한 레코드 리스트에 포함된 Hub ID 값들을 중복 제거하여 Set으로 반환
     */
    private static Set<UUID> getUniqueHubIds(Page<ProductDetailsQuerydslResponseDto> page) {
        return page.getContent().stream()
                .map(ProductDetailsQuerydslResponseDto::hubId)
                .collect(Collectors.toSet());
    }

    /**
     * 조회한 레코드 리스트에 포함된 Company ID 값들을 중복 제거하여 Set으로 반환
     */
    private static Set<UUID> getUniqueCompanyIds(Page<ProductDetailsQuerydslResponseDto> page) {
        return page.getContent().stream()
                .map(ProductDetailsQuerydslResponseDto::companyId)
                .collect(Collectors.toSet());
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
