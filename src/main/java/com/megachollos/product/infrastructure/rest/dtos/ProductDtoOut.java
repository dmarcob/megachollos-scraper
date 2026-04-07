package com.megachollos.product.infrastructure.rest.dtos;

import com.megachollos.product.domain.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@With
@Slf4j
public class ProductDtoOut {

  private String ecommerce;
  private String reference;

  private String title;
  private BigDecimal price;
  private BigDecimal originalPrice;
  private BigDecimal discount;

  private String brand;
  private String url;
  private String imageUrl;

  private LocalDateTime createdAt;

  public static ProductDtoOut fromDomain(Product product) {
    return ProductDtoOut.builder()
        .ecommerce(product.getEcommerce().getDisplayName())
        .reference(product.getReference())

        .title(product.getTitle())
        .price(product.getPrice())
        .originalPrice(product.getOriginalPrice())
        .discount(product.getDiscount())

        .brand(product.getBrand().getDisplayName())
        // URL completa
        .url(product.getUrl())
        .imageUrl(product.getImageUrl())

        .createdAt(product.getCreatedAt())
        .build();
  }
}
