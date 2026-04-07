package com.megachollos.testUtils.providers;

import static com.megachollos.testUtils.providers.BrandProvider.getBrand;
import static com.megachollos.testUtils.providers.ModelProvider.getModel;

import com.megachollos.ecommerce.domain.Ecommerce;
import com.megachollos.product.domain.Product;
import com.megachollos.product.domain.ProductState;
import com.megachollos.product.infrastructure.elastic.ProductDocument;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductProvider {

  public static final String REFERENCE = "134029164";
  public static final String TITLE = "Tarjeta Micro SD - SANDISK SDSQUNR-128G-GN6TA";
  public static final String URL = "/es/product/_tarjeta-micro-sd-sdsqunr-128g-gn6ta-sandisk-negro-134029164.html";
  public static final String IMAGE_URL = "https://site.com/image";
  public static final BigDecimal PRICE = BigDecimal.valueOf(70.00);
  public static final BigDecimal ORIGINAL_PRICE = BigDecimal.valueOf(100.00);
  public static final BigDecimal DISCOUNT = BigDecimal.valueOf(30.00);
  public static final LocalDateTime TIMESTAMP = LocalDateTime.of(2021, 1, 1, 0, 0);

  public static Product getProduct() {
    return Product.builder()
        .ecommerce(Ecommerce.MEDIAMARKT)
        .reference(REFERENCE)

        .state(ProductState.OK)
        .title(TITLE)
        .price(PRICE)
        .price(PRICE)
        .originalPrice(ORIGINAL_PRICE)
        .discount(DISCOUNT)

        .model(getModel())
        .brand(getBrand())
        .url(URL)
        .imageUrl(IMAGE_URL)

        .createdAt(TIMESTAMP)
        .build();
  }

  public static ProductDocument getProductDocument() {
    return ProductDocument.builder()
        .ecommerce(Ecommerce.MEDIAMARKT.getUniqueName())
        .reference(REFERENCE)

        .state(ProductState.OK.getValue())
        .title(TITLE)
        .price(PRICE)
        .originalPrice(ORIGINAL_PRICE)
        .discount(DISCOUNT)

        .model(getModel().getId())
        .brand(getBrand().getUniqueName())
        .url(URL)
        .imageUrl(IMAGE_URL)

        .createdAt(TIMESTAMP.toString())
        .build();
  }
}
