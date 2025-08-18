package com.tunaforce.product.controller;

import com.tunaforce.product.dto.request.ProductCreateRequestDto;
import com.tunaforce.product.dto.response.ProductFindPageResponseDto;
import com.tunaforce.product.entity.SortType;
import com.tunaforce.product.entity.UserRole;
import com.tunaforce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public ResponseEntity<ProductFindPageResponseDto> findProducts(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String productName,
            @RequestHeader("X-USER-ID") UUID userId, // FIXME 임시
            @RequestHeader("X-USER-ROLE") String userRole // FIXME 임시
    ) {
        SortType.validate(pageable.getSort());

        ProductFindPageResponseDto productPage
                = productService.findProductPage(pageable, productName, userId, UserRole.of(userRole));

        return ResponseEntity.ok()
                .body(productPage);
    }
}
