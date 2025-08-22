package com.tunaforce.product.repository.querydsl;

import com.tunaforce.product.dto.response.ProductSimpleResponseDto;
import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductQuerydslRepository {

    /**
     * 상품 단건 조회 쿼리
     */
    Optional<ProductDetailsQuerydslResponseDto> getProductDetails(UUID productId);

    Optional<ProductSimpleResponseDto> getProductSimpleDetails(UUID productId);

    List<ProductSimpleResponseDto> getProductSimpleList(List<UUID> productIds);

    /**
     * 주문용 상품 전체 조회 쿼리
     */
    Page<ProductDetailsQuerydslResponseDto> findPage(Pageable pageable, String productName);

    /**
     * 허브 담당자용 허브 내 업체 등록 상품 조회 쿼리
     */
    Page<ProductDetailsQuerydslResponseDto> findPageForHub(Pageable pageable, UUID hubId, String productName);

    /**
     * 업체 담당자용 업체 등록 상품 조회 쿼리
     */
    Page<ProductDetailsQuerydslResponseDto> findPageForCompany(Pageable pageable, UUID companyId, String productName);
}
