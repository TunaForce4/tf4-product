package com.tunaforce.product.service;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import com.tunaforce.product.dto.request.ProductUpdateOrderQuantityRequestDto;
import com.tunaforce.product.dto.request.ProductSimpleListRequestDto;
import com.tunaforce.product.dto.request.ProductSimpleReduceStockRequestDto;
import com.tunaforce.product.dto.request.ProductSimpleRequestDto;
import com.tunaforce.product.dto.response.ProductUpdateOrderQuantityResponseDto;
import com.tunaforce.product.dto.response.ProductSimpleListResponseDto;
import com.tunaforce.product.dto.response.ProductSimpleReduceStockResponseDto;
import com.tunaforce.product.dto.response.ProductSimpleResponseDto;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.repository.jpa.ProductJpaRepository;
import com.tunaforce.product.repository.querydsl.ProductQuerydslRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductInternalService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQuerydslRepository productQuerydslRepository;

    public ProductSimpleResponseDto findProduct(UUID productId) {

        return productQuerydslRepository.getProductSimpleDetails(productId)
                .orElseThrow(() -> new CustomRuntimeException(ProductException.PRODUCT_NOT_FOUND));
    }

    public ProductSimpleListResponseDto findProducts(ProductSimpleListRequestDto request) {
        List<UUID> productIds = request.productIds().stream()
                .map(ProductSimpleRequestDto::productId)
                .toList();

        List<ProductSimpleResponseDto> data = productQuerydslRepository.getProductSimpleList(productIds);

        return new ProductSimpleListResponseDto(data);
    }

    @Transactional
    public ProductSimpleReduceStockResponseDto reduceStock(UUID productId, ProductSimpleReduceStockRequestDto request) {
        Product product = findById(productId);

        product.reduceStock(request.quantity());
        int totalPrice = product.calculatePrice(request.quantity());

        return new ProductSimpleReduceStockResponseDto(product.getCompanyId(), totalPrice);
    }

    @Transactional
    public ProductUpdateOrderQuantityResponseDto updateOrderQuantity(UUID productId, ProductUpdateOrderQuantityRequestDto request) {
        Product product = findById(productId);

        Integer originalQuantity = request.originalQuantity();
        Integer updateQuantity = request.updateQuantity();

        if (originalQuantity.equals(updateQuantity)) {
            int currentPrice = product.calculatePrice(originalQuantity);
            return new ProductUpdateOrderQuantityResponseDto(currentPrice);
        }

        // 수량 감소 - 환불
        if (updateQuantity < originalQuantity) {
            int decreaseQuantity = originalQuantity - updateQuantity;
            product.increaseStock(decreaseQuantity);
        }

        // 수량 증가 - 추가 결제
        if (updateQuantity > originalQuantity) {
            int increaseQuantity = updateQuantity - originalQuantity;
            product.reduceStock(increaseQuantity);
        }

        int finalPrice = product.calculatePrice(updateQuantity);
        return new ProductUpdateOrderQuantityResponseDto(finalPrice);
    }

    private Product findById(UUID productId) {
        return productJpaRepository.findById(productId)
                .orElseThrow(() -> new CustomRuntimeException(ProductException.PRODUCT_NOT_FOUND));
    }
}
