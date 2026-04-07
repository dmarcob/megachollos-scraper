package com.megachollos.product.domain;

import com.megachollos.brand.domain.Brand;
import com.megachollos.ecommerce.domain.IEcommerce;
import com.megachollos.model.domain.Model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
@Slf4j
public class Product {

  private IEcommerce ecommerce;
  private String reference;
  private ProductState state;
  private ProductErrorReason errorReason;

  private String title;
  private BigDecimal price;
  private BigDecimal originalPrice;
  private BigDecimal discount;

  private Model model;
  private Brand brand;
  private String url;
  private String imageUrl;

  private LocalDateTime createdAt;

  /**
   * Elimina el base path de la url (solo si lo tiene) y la setea
   */
  public void setUrl(String url, String basePath) {
    if (url != null && !url.isEmpty()) {
      if (!url.startsWith("http://") && !url.startsWith("https://")) {
        this.url = url;
      } else {
        this.url = url.substring(basePath.length());
      }
    }
  }

  /**
   * Calcula y setea el descuento
   */
  public void setDiscount() {
    if (originalPrice == null) {
      log.warn("Original price must be not null -> Discount is set to zero");
      this.discount = BigDecimal.ZERO;
      return;
    }

    if (price == null || originalPrice.subtract(price).compareTo(BigDecimal.ZERO) <= 0) {
      log.warn("price must be not null and smaller than original price -> Discount is set to zero");
      this.discount = BigDecimal.ZERO;
      return;
    }

    BigDecimal difference = originalPrice.subtract(price);
    this.discount = difference
        .divide(originalPrice, 2, RoundingMode.HALF_UP)
        .multiply(new BigDecimal("100"));
  }

  /**
   * Asigna el estado del producto
   */
  public void setState() {
    if (state != null) {
      log.debug("Product has already state {}, skip", state);
      return;
    }

    if (StringUtils.isEmpty(reference)) {
      log.warn("Product has no reference, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_REFERENCE);
      return;
    }

    if (StringUtils.isEmpty(title)) {
      log.warn("Product has no title, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_TITLE);
      return;
    }

    if (StringUtils.isEmpty(url)) {
      log.warn("Product has no url, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_URL);
      return;
    }

    if (price == null) {
      log.warn("Product has no price, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_PRICE);
      return;
    }

    if (originalPrice == null) {
      log.warn("Product has no original price, set to ERROR {}",
          getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_ORIGINAL_PRICE);
      return;
    }

    if (discount == null) {
      log.warn("Product has no discount, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.MISSING_DISCOUNT);
      return;
    }

    if (discount.compareTo(BigDecimal.ZERO) <= 0) {
      log.warn("Product has no discount, set to ERROR {}", getEcommerce().getUniqueName() + ":" + getUrl());
      toErrorState(ProductErrorReason.ZERO_DISCOUNT);
      return;
    }

    log.debug("Product has valid discounted price, set to OK {}",
        getEcommerce().getUniqueName() + ":" + getUrl());
    this.state = ProductState.OK;
    this.errorReason = null;
  }

  public Product toErrorState(ProductErrorReason errorReason) {
    this.setState(ProductState.ERROR);
    this.setErrorReason(errorReason);
    return this;
  }

  public boolean hasErrorState() {
    return state != null && state.equals(ProductState.ERROR);
  }

}
