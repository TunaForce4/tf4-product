package com.tunaforce.product.service;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.dto.request.ProductUpdateRequestDto;
import com.tunaforce.product.dto.response.ProductDeleteResponseDto;
import com.tunaforce.product.dto.response.ProductFindDetailResponseDto;
import com.tunaforce.product.dto.response.ProductFindPageResponseDto;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.entity.UserRole;
import com.tunaforce.product.repository.feign.company.CompanyFeignClient;
import com.tunaforce.product.repository.feign.company.dto.request.CompanyFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoListResponse;
import com.tunaforce.product.repository.feign.company.dto.response.CompanyFindInfoResponseDto;
import com.tunaforce.product.repository.feign.hub.HubFeignClient;
import com.tunaforce.product.repository.feign.hub.dto.request.HubFindInfoListRequestDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoListResponseDto;
import com.tunaforce.product.repository.feign.hub.dto.response.HubFindInfoResponseDto;
import com.tunaforce.product.repository.jpa.ProductJpaRepository;
import com.tunaforce.product.repository.querydsl.ProductQuerydslRepository;
import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final HubFeignClient hubFeignClient;
    private final CompanyFeignClient companyFeignClient;

    private final ProductJpaRepository productJpaRepository;
    private final ProductQuerydslRepository productQuerydslRepository;

    /**
     * 상품 생성 메인 서비스 로직
     */
    public void createProduct(ProductCreateRequestDto request, UUID userId, UserRole role) {
        validateCreateProductByAuthority(request, userId, role);

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
     * 상품 단건 조회 메인 서비스 로직
     */
    public ProductFindDetailResponseDto findProductDetails(UUID productId, UUID userId, UserRole userRole) {
        ProductDetailsQuerydslResponseDto productDetails = productQuerydslRepository.getProductDetails(productId);

        validateFindProductDetailsByAuthority(productDetails.hubId(), productDetails.companyId(), userId, userRole);

        Set<UUID> uniqueHubIds = getUniqueHubIds(List.of(productDetails));
        Set<UUID> uniqueCompanyIds = getUniqueCompanyIds(List.of(productDetails));

        Map<UUID, String> hubs = getHubs(uniqueHubIds);
        Map<UUID, String> companies = getCompanies(uniqueCompanyIds);

        return ProductFindDetailResponseDto.from(productDetails, hubs, companies);
    }

    /**
     * 주문 용 전체 상품 페이지네이션 메인 서비스 로직
     */
    public ProductFindPageResponseDto findProductPageForOrder(
            Pageable pageable,
            String productName
    ) {
        Page<ProductDetailsQuerydslResponseDto> page = productQuerydslRepository.findPage(pageable, productName);

        return mapPageToResponse(page);
    }

    /**
     * 허브 소속 업체들이 등록한 상품 페이지네이션 메인 서비스 로직
     */
    public ProductFindPageResponseDto findProductPageByHub(
            Pageable pageable,
            UUID hubId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        validateFindHubPageByAuthority(hubId, userId, userRole);

        Page<ProductDetailsQuerydslResponseDto> page
                = productQuerydslRepository.findPageForHub(pageable, hubId, productName);

        return mapPageToResponse(page);
    }

    /**
     * 업체가 등록한 상품 페이지네이션 메인 서비스 로직
     */
    public ProductFindPageResponseDto findProductPageByCompany(
            Pageable pageable,
            UUID companyId,
            String productName,
            UUID userId,
            UserRole userRole
    ) {
        validateFindCompanyProductPageByAuthority(companyId, userId, userRole);

        Page<ProductDetailsQuerydslResponseDto> page
                = productQuerydslRepository.findPageForCompany(pageable, companyId, productName);

        return mapPageToResponse(page);
    }

    /**
     * 상품 수정 메인 서비스 로직
     */
    @Transactional
    public void updateProduct(UUID productId, ProductUpdateRequestDto request, UUID userId, UserRole role) {
        Product product = findProductById(productId);
        validateUpdateProductByAuthority(product.getHubId(), product.getCompanyId(), userId, role);

        product.update(request);
    }

    /**
     * 상품 삭제 메인 서비스 로직
     */
    @Transactional
    public ProductDeleteResponseDto deleteProduct(UUID productId, UUID userId, UserRole role) {
        Product product = findProductById(productId);
        validateDeleteProductByAuthority(product.getHubId(), userId, role);

        product.delete(userId);

        return new ProductDeleteResponseDto(true);
    }

    /**
     * 상품 등록 유저 권한 검증
     */
    private void validateCreateProductByAuthority(ProductCreateRequestDto request, UUID userId, UserRole role) {
        if (role.equals(UserRole.DELIVERY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        CompanyFindInfoResponseDto requestedCompany = companyFeignClient.findCompanyInfoByCompanyId(request.companyId());

        // 등록 요청한 업체가 요청한 허브의 소속 업체인지 확인
        validateUuidMatch(request.hubId(), requestedCompany.hubId());

        // 허브 담당자의 경우 - 로그인한 유저의 허브 Id와 등록 요청한 허브 Id 확인
        if (role.equals(UserRole.HUB)) {
            HubFindInfoResponseDto userHub = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(userHub.hubId(), requestedCompany.hubId());
        }

        // 업체 담당자의 경우 - 로그인한 유저의 업체 Id와 등록 요청한 업체 Id 확인
        if (role.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto userCompany = companyFeignClient.findCompanyInfoByUserId(userId);
            validateUuidMatch(userCompany.companyId(), requestedCompany.companyId());
        }
    }

    /**
     * 상품 단건 조회 유저 권한 검증
     */
    private void validateFindProductDetailsByAuthority(UUID productHubId, UUID productCompanyId, UUID userId, UserRole userRole) {
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto userHub = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(userHub.hubId(), productHubId);
        }

        if (userRole.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto userCompany = companyFeignClient.findCompanyInfoByUserId(userId);
            validateUuidMatch(userCompany.companyId(), productCompanyId);
        }
    }

    /**
     * 특정 허브 소속 업체들의 등록 상품에 대한 권한별 조회
     */
    private void validateFindHubPageByAuthority(UUID requestedHubId, UUID userId, UserRole userRole) {
        if (userRole.equals(UserRole.COMPANY) || userRole.equals(UserRole.DELIVERY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        // 로그인한 유저가 허브 담당자 일 때 요청한 허브에 접근 가능한지 확인
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto userHub = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(userHub.hubId(), requestedHubId);
        }
    }

    /**
     * 특정 업체의 등록 상품에 대한 권한별 조회
     */
    private void validateFindCompanyProductPageByAuthority(UUID requestedCompanyId, UUID userId, UserRole userRole) {
        if (userRole.equals(UserRole.DELIVERY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        // 로그인한 유저가 허브 담당자 일 때 요청한 업체가 소속 업체인지 확인
        if (userRole.equals(UserRole.HUB)) {
            HubFindInfoResponseDto userHub = hubFeignClient.findHubInfoByUserId(userId);
            CompanyFindInfoResponseDto requestedCompany = companyFeignClient.findCompanyInfoByCompanyId(requestedCompanyId);
            validateUuidMatch(userHub.hubId(), requestedCompany.hubId());
        }

        // 로그인한 유저가 업체 담당자 일 때 자신의 업체인지 확인
        if (userRole.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto userCompany = companyFeignClient.findCompanyInfoByUserId(userId);
            validateUuidMatch(userCompany.companyId(), requestedCompanyId);
        }
    }

    /**
     * 상품 수정 유저 권한 검증
     */
    private void validateUpdateProductByAuthority(
            UUID productHubId,
            UUID productCompanyId,
            UUID userId,
            UserRole role
    ) {
        if (role.equals(UserRole.DELIVERY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        if (role.equals(UserRole.HUB)) {
            HubFindInfoResponseDto userHub = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(userHub.hubId(), productHubId);
        }

        if (role.equals(UserRole.COMPANY)) {
            CompanyFindInfoResponseDto userCompany = companyFeignClient.findCompanyInfoByUserId(userId);
            validateUuidMatch(userCompany.companyId(), productCompanyId);
        }
    }

    /**
     * 상품 삭제 유저 권한 검증
     */
    private void validateDeleteProductByAuthority(UUID productHubId, UUID userId, UserRole role) {
        // 등록 상품 삭제는 마스터 또는 허브 관리자만 가능
        if (role.equals(UserRole.COMPANY) || role.equals(UserRole.DELIVERY)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }

        if (role.equals(UserRole.HUB)) {
            HubFindInfoResponseDto hubInfo = hubFeignClient.findHubInfoByUserId(userId);
            validateUuidMatch(hubInfo.hubId(), productHubId);
        }
    }

    /**
     * 두 UUID 값을 비교, 두 값이 다르면 throw ACCESS_DENIED exception
     */
    private void validateUuidMatch(UUID expectedId, UUID actualId) {
        if (!expectedId.equals(actualId)) {
            throw new CustomRuntimeException(ProductException.ACCESS_DENIED);
        }
    }

    /**
     * 조회한 Page 객체를 ProductFindPageResponseDto로 매핑
     */
    private ProductFindPageResponseDto mapPageToResponse(Page<ProductDetailsQuerydslResponseDto> page) {
        // 조회한 레코드에서 허브와 업체 ID 중복 제거
        Set<UUID> hubSet = getUniqueHubIds(page.getContent());
        Set<UUID> companySet = getUniqueCompanyIds(page.getContent());

        // 허브와 업체 정보(이름) 조회
        Map<UUID, String> hubs = getHubs(hubSet);
        Map<UUID, String> companies = getCompanies(companySet);

        return ProductFindPageResponseDto.from(page, hubs, companies);
    }

    /**
     * 조회한 레코드 리스트에 포함된 Hub ID 값들을 중복 제거하여 Set으로 반환
     */
    private Set<UUID> getUniqueHubIds(List<ProductDetailsQuerydslResponseDto> data) {
        return data.stream()
                .map(ProductDetailsQuerydslResponseDto::hubId)
                .collect(Collectors.toSet());
    }

    /**
     * 조회한 레코드 리스트에 포함된 Company ID 값들을 중복 제거하여 Set으로 반환
     */
    private Set<UUID> getUniqueCompanyIds(List<ProductDetailsQuerydslResponseDto> data) {
        return data.stream()
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

        HubFindInfoListRequestDto requestDto = HubFindInfoListRequestDto.from(hubSet.stream().toList());
        HubFindInfoListResponseDto hubs = hubFeignClient.findHubInfoListByHubIds(requestDto);

        return hubs.toMap();
    }

    /**
     * 업체 이름 조회
     */
    private Map<UUID, String> getCompanies(Set<UUID> companySet) {
        if (companySet.isEmpty()) {
            return Collections.emptyMap();
        }

        CompanyFindInfoListRequestDto requestDto = CompanyFindInfoListRequestDto.from(companySet.stream().toList());
        CompanyFindInfoListResponse companies = companyFeignClient.findCompanyInfoListByCompanyIds(requestDto);

        return companies.toMap();
    }

    private Product findProductById(UUID productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new CustomRuntimeException(ProductException.PRODUCT_NOT_FOUND));
    }
}
