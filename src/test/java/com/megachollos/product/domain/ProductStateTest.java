package com.megachollos.product.domain;

import static com.megachollos.product.domain.exceptions.errors.ProductError.PRODUCT_STATE_NOT_EXIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.megachollos.shared.domain.exceptions.InternalException;
import com.megachollos.testUtils.bases.BaseUnitTest;
import org.junit.jupiter.api.Test;

public class ProductStateTest extends BaseUnitTest {

  @Test
  void fromValue_OK() {
    // WHEN
    ProductState result = ProductState.fromValue(ProductState.OK.getValue());

    // THEN
    assertThat(result).isEqualTo(ProductState.OK);
  }

  @Test
  void fromValue_noExist_throwException() {
    // WHEN, THEN
    InternalException exception = assertThrows(InternalException.class,
        () -> ProductState.fromValue("NOT_STATE"));

    // THEN
    assertThat(exception.getCode()).isEqualTo(PRODUCT_STATE_NOT_EXIST.getCode());
  }
}