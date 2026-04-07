package com.megachollos.product.domain;

import static com.megachollos.testUtils.providers.ProductProvider.getProduct;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.testUtils.bases.BaseUnitTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ProductTest extends BaseUnitTest {

  public static Stream<Arguments> setDiscountArgumentProvider() {
    return Stream.of(
        // Happy path
        Arguments.of(
            BigDecimal.valueOf(100),
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(90).setScale(2, RoundingMode.HALF_UP)
        ),
        // Original price null
        Arguments.of(
            null,
            BigDecimal.valueOf(10),
            BigDecimal.ZERO
        ),
        // price null
        Arguments.of(
            BigDecimal.valueOf(100),
            null,
            BigDecimal.ZERO
        ),
        // Price greater or equal than original price
        Arguments.of(
            BigDecimal.ZERO,
            BigDecimal.valueOf(10),
            BigDecimal.ZERO
        ),
        Arguments.of(
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(10),
            BigDecimal.ZERO
        )
    );
  }

  public static Stream<Arguments> toErrorArgumentProvider() {
    return Stream.of(
        Arguments.of(
            getProduct().withState(null).withReference(null),
            ProductErrorReason.MISSING_REFERENCE
        ),
        Arguments.of(
            getProduct().withState(null).withTitle(null),
            ProductErrorReason.MISSING_TITLE
        ),
        Arguments.of(
            getProduct().withState(null).withUrl(null),
            ProductErrorReason.MISSING_URL
        ),
        Arguments.of(
            getProduct().withState(null).withPrice(null),
            ProductErrorReason.MISSING_PRICE
        ),
        Arguments.of(
            getProduct().withState(null).withOriginalPrice(null),
            ProductErrorReason.MISSING_ORIGINAL_PRICE
        ),
        Arguments.of(
            getProduct().withState(null).withDiscount(null),
            ProductErrorReason.MISSING_DISCOUNT
        ),
        Arguments.of(
            getProduct().withState(null).withDiscount(BigDecimal.ZERO),
            ProductErrorReason.ZERO_DISCOUNT
        )
    );
  }

  @ParameterizedTest
  @MethodSource("setDiscountArgumentProvider")
  void setDiscount_OK(BigDecimal originalPrice, BigDecimal price, BigDecimal expectedDiscount) {
    // GIVEN
    Product product = Product.builder()
        .originalPrice(originalPrice)
        .price(price)
        .build();

    // WHEN
    product.setDiscount();

    // THEN
    assertThat(product.getDiscount()).isEqualTo(expectedDiscount);
  }

  @Test
  void setState_toOK() {
    // GIVEN
    Product product = getProduct().withState(null);

    // WHEN
    product.setState();

    // THEN
    assertThat(product.getState()).isEqualTo(ProductState.OK);
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("toErrorArgumentProvider")
  void setState_toError(Product product, ProductErrorReason expectedErrorReason) {
    // WHEN
    product.setState();

    // THEN
    assertThat(product.getState()).isEqualTo(ProductState.ERROR);
    assertThat(product.getErrorReason()).isEqualTo(expectedErrorReason);
  }
}