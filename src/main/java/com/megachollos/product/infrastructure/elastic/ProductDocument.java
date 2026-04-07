package com.megachollos.product.infrastructure.elastic;

import static com.megachollos.brand.domain.errors.BrandError.BRAND_NOT_EXIST;
import static com.megachollos.ecommerce.domain.exceptions.errors.EcommerceError.ECOMMERCE_NOT_EXIST;
import static com.megachollos.model.domain.exceptions.errors.ModelError.MODEL_NOT_EXIST;

import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.ecommerce.domain.Ecommerce;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.product.domain.Product;
import com.megachollos.product.domain.ProductErrorReason;
import com.megachollos.product.domain.ProductState;
import com.megachollos.shared.domain.exceptions.InternalException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ProductDocument {

  private String ecommerce;
  private String reference;

  private String state;
  private String errorReason;
  private String title;
  private BigDecimal price;
  private BigDecimal originalPrice;
  private BigDecimal discount;

  private Long model;
  private String brand;
  private String url;
  private String imageUrl;

  private String createdAt;

  @Id
  @AccessType(AccessType.Type.PROPERTY)
  public String getElasticsearchId() {
    return ecommerce + '@' + reference;
  }

  public Product toDomain(ModelRepository modelRepository, BrandRepository brandRepository) {
    return Product.builder()
        .ecommerce(Ecommerce.fromValue(ecommerce)
            .orElseThrow(() -> new InternalException(ECOMMERCE_NOT_EXIST)))
        .reference(reference)

        .state(ProductState.fromValue(state))
        .errorReason(errorReason == null ? null : ProductErrorReason.fromValue(errorReason))
        .title(title)
        .price(price)
        .originalPrice(originalPrice)
        .discount(discount)

        .model(model == null ? null : modelRepository.findById(model)
            .orElseThrow(() -> {
              log.warn("Model with id {} does not exist", model);
              return new InternalException(MODEL_NOT_EXIST, model);
            }))
        .brand(brand == null ? null : brandRepository.findByUniqueName(brand)
            .orElseThrow(() -> {
              log.warn("Brand with uniqueName {} does not exist", brand);
              return new InternalException(BRAND_NOT_EXIST, brand);
            }))
        .url(url)
        .imageUrl(imageUrl)

        .createdAt(createdAt == null ? null : LocalDateTime.parse(createdAt))
        .build();
  }

  public static ProductDocument fromDomain(Product product) {
    return ProductDocument.builder()
        .ecommerce(product.getEcommerce() == null ? null : product.getEcommerce().getUniqueName())
        .reference(product.getReference())

        .state(product.getState() == null ? null : product.getState().getValue())
        .errorReason(product.getErrorReason() == null ? null : product.getErrorReason().getValue())
        .title(product.getTitle())
        .price(product.getPrice())
        .originalPrice(product.getOriginalPrice())
        .discount(product.getDiscount())

        .model(product.getModel() == null ? null : product.getModel().getId())
        .brand(product.getBrand() == null ? null : product.getBrand().getUniqueName())
        .url(product.getUrl())
        .imageUrl(product.getImageUrl())

        .createdAt(product.getCreatedAt() == null ? null : product.getCreatedAt().toString())
        .build();
  }
}
