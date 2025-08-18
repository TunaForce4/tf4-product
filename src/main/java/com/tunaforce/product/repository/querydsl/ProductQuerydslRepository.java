package com.tunaforce.product.repository.querydsl;

import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductQuerydslRepository {

    Page<ProductDetailsQuerydslResponseDto> findPage(Pageable pageable, UUID hubId, UUID companyId, String productName);
}
