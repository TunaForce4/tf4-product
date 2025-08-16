package com.tunaforce.product.service;

import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.repository.feign.auth.AuthFeignClient;
import com.tunaforce.product.repository.feign.company.CompanyFeignClient;
import com.tunaforce.product.repository.feign.hub.HubFeignClient;
import com.tunaforce.product.repository.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final HubFeignClient hubFeignClient;
    private final AuthFeignClient authFeignClient;
    private final CompanyFeignClient companyFeignClient;
    private final ProductJpaRepository productJpaRepository;

    public void createProduct(ProductCreateRequestDto request, UUID userId) {
        // check id values
//        authFeignClient.existsById(userId);
//        companyFeignClient.existsById(request.companyId());
//        hubFeignClient.existsByIdWithCompany(request.hubId(), request.companyId());

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
