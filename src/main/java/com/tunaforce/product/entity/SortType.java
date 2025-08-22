package com.tunaforce.product.entity;

import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum SortType {

    CREATED_ASC("createdAt", Sort.Direction.ASC),
    CREATED_DESC("createdAt", Sort.Direction.DESC),
    UPDATED_ASC("updatedAt", Sort.Direction.ASC),
    UPDATED_DESC("updatedAt", Sort.Direction.DESC),
    PRICE_ASC("price", Sort.Direction.ASC),
    PRICE_DESC("price", Sort.Direction.DESC),
    ;

    private final String value;
    private final Sort.Direction direction;

    public static void validate(Sort sort) {
        Set<String> check = new HashSet<>();

        for (Sort.Order order : sort) {
            if (!match(order)) {
                throw new CustomRuntimeException(ProductException.UNSUPPORTED_SORT_TYPE);
            }

            if (!check.add(order.getProperty().toLowerCase())) {
                throw new CustomRuntimeException(ProductException.DUPLICATED_SORT_TYPE);
            }
        }
    }

    private static boolean match(Sort.Order order) {
        return Arrays.stream(SortType.values())
                .anyMatch(sortType -> sortType.value.equalsIgnoreCase(order.getProperty()) &&
                        sortType.getDirection().equals(order.getDirection()));
    }
}
