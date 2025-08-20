package com.tunaforce.product.repository.querydsl;

import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductQuerydslRepository {

    ProductDetailsQuerydslResponseDto getProductDetails(UUID productId);

    Page<ProductDetailsQuerydslResponseDto> findPage(Pageable pageable, String productName);

    Page<ProductDetailsQuerydslResponseDto> findPageForHub(Pageable pageable, UUID hubId, String productName);

    Page<ProductDetailsQuerydslResponseDto> findPageForCompany(Pageable pageable, UUID companyId, String productName);
}
