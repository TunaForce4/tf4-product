package com.tunaforce.product.repository.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tunaforce.product.dto.response.ProductSimpleResponseDto;
import com.tunaforce.product.dto.response.QProductSimpleResponseDto;
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
import java.util.Optional;
import java.util.UUID;

import static com.tunaforce.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQuerydslRepositoryImpl implements ProductQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ProductDetailsQuerydslResponseDto> getProductDetails(UUID productId) {
        ProductDetailsQuerydslResponseDto record = queryFactory.select(new QProductDetailsQuerydslResponseDto(
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
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(record);
    }

    @Override
    public Optional<ProductSimpleResponseDto> getProductSimpleDetails(UUID productId) {
        ProductSimpleResponseDto record = queryFactory.select(new QProductSimpleResponseDto(
                        product.id,
                        product.name
                ))
                .from(product)
                .where(product.id.eq(productId))
                .fetchOne();

        return Optional.ofNullable(record);
    }

    @Override
    public List<ProductSimpleResponseDto> getProductSimpleList(List<UUID> productIds) {
        return queryFactory.select(new QProductSimpleResponseDto(
                        product.id,
                        product.name
                ))
                .from(product)
                .where(product.id.in(productIds))
                .fetch();
    }

    @Override
    public Page<ProductDetailsQuerydslResponseDto> findPage(
            Pageable pageable,
            String productName
    ) {
        Predicate[] whereClause = {
                eqProductName(productName),
                product.deletedAt.isNull()
        };

        return executeQuery(pageable, whereClause);
    }

    @Override
    public Page<ProductDetailsQuerydslResponseDto> findPageForHub(Pageable pageable, UUID hubId, String productName) {
        Predicate[] whereClause = {
                eqHubId(hubId),
                eqProductName(productName),
                product.deletedAt.isNull()
        };

        return executeQuery(pageable, whereClause);
    }

    @Override
    public Page<ProductDetailsQuerydslResponseDto> findPageForCompany(Pageable pageable, UUID companyId, String productName) {
        Predicate[] whereClause = {
                eqCompanyId(companyId),
                eqProductName(productName),
                product.deletedAt.isNull()
        };

        return executeQuery(pageable, whereClause);
    }

    private Page<ProductDetailsQuerydslResponseDto> executeQuery(Pageable pageable, Predicate[] whereClause) {
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
                .where(whereClause)
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // COUNT
        JPAQuery<Long> count = queryFactory
                .select(product.count())
                .where(whereClause)
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
