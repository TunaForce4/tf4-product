package com.tunaforce.product.entity;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    MASTER("MASTER"),
    COMPANY("COMPANY"),
    HUB("HUB"),
    DELIVERY("DELIVERY"),
    ;

    private final String roleName;

    public static UserRole of(String roleName) {
        return Stream.of(UserRole.values())
                .filter(role -> role.roleName.equalsIgnoreCase(roleName))
                .findFirst().orElseThrow(() -> new CustomRuntimeException(ProductException.ACCESS_DENIED));
    }
}
