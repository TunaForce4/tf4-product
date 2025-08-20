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
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Roles") String userRole
    ) {
        UserRole role = UserRole.of(userRole);

        productService.createProduct(productCreateRequestDto, userId, role);

        return ResponseEntity.created(null)
                .body(null);
    }

    @GetMapping
    public ResponseEntity<ProductFindPageResponseDto> findProductsForOrder(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String productName
    ) {
        SortType.validate(pageable.getSort());

        ProductFindPageResponseDto productPage
                = productService.findProductPageForOrder(pageable, productName);

        return ResponseEntity.ok()
                .body(productPage);
    }

    @GetMapping("/hubs/{hubId}")
    public ResponseEntity<ProductFindPageResponseDto> findProductsByHubManager(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable UUID hubId,
            @RequestParam(required = false) String productName,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Roles") String userRole
    ) {
        SortType.validate(pageable.getSort());
        UserRole role = UserRole.of(userRole);

        ProductFindPageResponseDto data
                = productService.findProductPageByHub(pageable, hubId, productName, userId, role);

        return ResponseEntity.ok()
                .body(data);
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<ProductFindPageResponseDto> findProductsByCompanyManager(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable UUID companyId,
            @RequestParam(required = false) String productName,
            @RequestHeader("X-User-Id") UUID userId,
            @RequestHeader("X-Roles") String userRole
    ) {
        SortType.validate(pageable.getSort());
        UserRole role = UserRole.of(userRole);

        ProductFindPageResponseDto data
                = productService.findProductPageByCompany(pageable, companyId, productName, userId, role);

        return ResponseEntity.ok()
                .body(data);
    }
}
