package com.tunaforce.product.controller;

import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Void> createProduct(
            @RequestBody @Valid ProductCreateRequestDto productCreateRequestDto,
            @RequestHeader("X-USER-ID") UUID userId // FIXME 임시
    ) {
        productService.createProduct(productCreateRequestDto, userId);

        return ResponseEntity.created(null)
                .body(null);
    }
}
