package com.tunaforce.product.controller;

import com.tunaforce.product.dto.request.ProductSimpleListRequestDto;
import com.tunaforce.product.dto.request.ProductSimpleReduceStockRequestDto;
import com.tunaforce.product.dto.request.ProductUpdateOrderQuantityRequestDto;
import com.tunaforce.product.dto.response.ProductSimpleListResponseDto;
import com.tunaforce.product.dto.response.ProductSimpleReduceStockResponseDto;
import com.tunaforce.product.dto.response.ProductSimpleResponseDto;
import com.tunaforce.product.dto.response.ProductUpdateOrderQuantityResponseDto;
import com.tunaforce.product.service.ProductInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products/internal")
@RequiredArgsConstructor
public class ProductInternalController {

    private final ProductInternalService productInternalService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductSimpleResponseDto> findProduct(
            @PathVariable UUID productId
    ) {
        ProductSimpleResponseDto data = productInternalService.findProduct(productId);

        return ResponseEntity.ok()
                .body(data);
    }

    @PostMapping
    public ResponseEntity<ProductSimpleListResponseDto> findProducts(
            @RequestBody ProductSimpleListRequestDto productSimpleListRequestDto
    ) {
        ProductSimpleListResponseDto data = productInternalService.findProducts(productSimpleListRequestDto);

        return ResponseEntity.ok()
                .body(data);
    }

    @PostMapping("/{productId}/reduce-stock")
    public ResponseEntity<ProductSimpleReduceStockResponseDto> reduceStock(
            @PathVariable UUID productId,
            @RequestBody ProductSimpleReduceStockRequestDto productSimpleReduceStockRequestDto
    ) {
        ProductSimpleReduceStockResponseDto data = productInternalService.reduceStock(productId, productSimpleReduceStockRequestDto);

        return ResponseEntity.ok()
                .body(data);
    }

    @PostMapping("/{productId}/update-order-quantity")
    public ResponseEntity<ProductUpdateOrderQuantityResponseDto> updateOrderQuantity(
            @PathVariable UUID productId,
            @RequestBody ProductUpdateOrderQuantityRequestDto productUpdateOrderQuantityRequestDto
    ) {
        ProductUpdateOrderQuantityResponseDto data = productInternalService.updateOrderQuantity(productId, productUpdateOrderQuantityRequestDto);

        return ResponseEntity.ok()
                .body(data);
    }
}
