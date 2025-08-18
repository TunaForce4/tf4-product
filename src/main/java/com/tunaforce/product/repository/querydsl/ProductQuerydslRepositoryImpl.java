package com.tunaforce.product.repository.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tunaforce.product.entity.Product;
import com.tunaforce.product.repository.querydsl.dto.response.ProductDetailsQuerydslResponseDto;
import com.tunaforce.product.repository.querydsl.dto.response.QProductDetailsQuerydslResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tunaforce.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepositoryImpl implements ProductQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductDetailsQuerydslResponseDto> findPage(
            Pageable pageable,
            UUID hubId,
            UUID companyId,
            String productName
    ) {
        // SELECT
        List<ProductDetailsQuerydslResponseDto> records = queryFactory
                .select(new QProductDetailsQuerydslResponseDto(
                        product.id,
                        product.hubId,
                        product.companyId,
                        product.name,
                        product.price,
                        product.quantity,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .where(
                        eqHubId(hubId),
                        eqCompanyId(companyId),
                        eqProductName(productName),
                        product.deletedAt.isNull()
                )
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // COUNT
        JPAQuery<Long> count = queryFactory
                .select(product.count())
                .where(
                        eqHubId(hubId),
                        eqCompanyId(companyId),
                        eqProductName(productName),
                        product.deletedAt.isNull()
                )
                .from(product);

        return PageableExecutionUtils.getPage(records, pageable, count::fetchOne);
    }

    private BooleanExpression eqHubId(UUID hubId) {
        return hubId != null ? product.hubId.eq(hubId) : null;
    }

    private BooleanExpression eqCompanyId(UUID companyId) {
        return companyId != null ? product.companyId.eq(companyId) : null;
    }

    private BooleanExpression eqProductName(String productName) {
        return StringUtils.hasText(productName) ? product.name.contains(productName) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order sortOrder : sort) {
            Order order = sortOrder.isAscending() ? Order.ASC : Order.DESC;
            PathBuilder<Product> pathBuilder = new PathBuilder<>(product.getType(), product.getMetadata());

            orderSpecifiers.add(new OrderSpecifier<>(order, pathBuilder.getString(sortOrder.getProperty())));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
