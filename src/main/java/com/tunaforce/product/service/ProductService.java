package com.tunaforce.product.service;

import com.tunaforce.product.dto.request.AuthCreateProductCheckUserAffiliationRequestDto;
import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.repository.feign.auth.AuthFeignClient;
import com.tunaforce.product.repository.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final AuthFeignClient authFeignClient;
    private final ProductJpaRepository productJpaRepository;

    public void createProduct(ProductCreateRequestDto request, UUID userId) {
        // 유저 역할에 따라 상품 생성에 요청된 허브 또는 업체와 로그인한 유저의 허브 또는 업체와의 관계가 유효한지 검증
        AuthCreateProductCheckUserAffiliationRequestDto authRequestDto =
                new AuthCreateProductCheckUserAffiliationRequestDto(
                        userId,
                        request.hubId(),
                        request.companyId()
                );

        authFeignClient.checkUserAffiliation(authRequestDto);

        // persist a product
        Product product = Product.builder()
                .companyId(request.companyId())
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();

        productJpaRepository.save(product);
    }
}
