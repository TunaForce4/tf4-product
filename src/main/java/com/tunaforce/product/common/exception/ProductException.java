package com.tunaforce.product.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductException {

    // Product
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다."),


    // Auth
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    AUTH_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스가 동작하지 않습니다."),

    // Company
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "업체 정보를 찾을 수 없습니다."),
    COMPANY_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스가 동작하지 않습니다."),

    // Hub
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "허브 정보를 찾을 수 없습니다."),
    INVALID_HUB_COMPANY_RELATION(HttpStatus.BAD_REQUEST, "선택한 업체는 해당 허브에 소속되어 있지 않습니다."),
    HUB_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스가 동작하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
