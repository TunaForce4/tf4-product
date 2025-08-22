package com.tunaforce.product.entity;

import com.tunaforce.product.common.entity.Timestamped;
import com.tunaforce.product.common.exception.CustomRuntimeException;
import com.tunaforce.product.common.exception.ProductException;
import com.tunaforce.product.dto.request.ProductUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Entity
@Table(name = "p_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Builder
    public Product(UUID hubId, UUID companyId, String name, Integer price, Integer quantity) {
        this.hubId = hubId;
        this.companyId = companyId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void update(ProductUpdateRequestDto request) {
        updateName(request.name());
        updatePrice(request.price());
        updateQuantity(request.quantity());
    }

    private void updateName(String name) {
        if (StringUtils.hasText(name)) {
            this.name = name;
        }
    }

    private void updatePrice(Integer price) {
        if (price != null) {
            this.price = price;
        }
    }

    private void updateQuantity(Integer quantity) {
        if (quantity != null) {
            this.quantity = quantity;
        }
    }


    public void reduceStock(int quantity) {
        if (this.quantity < quantity) {
            throw new CustomRuntimeException(ProductException.OUT_OF_STOCK);
        }

        this.quantity -= quantity;
    }

    public int calculatePrice(int quantity) {
        return this.price * quantity;
    }

    public void increaseStock(int restoreQuantity) {
        this.quantity += restoreQuantity;
    }
}
