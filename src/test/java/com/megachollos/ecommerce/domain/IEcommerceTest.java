package com.megachollos.ecommerce.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

public class IEcommerceTest extends BaseUnitTest {

  @Test
  void fromValue_throwException() {
    // WHEN, THEN
    assertThrows(UnsupportedOperationException.class, () -> IEcommerce.fromValue("value"));
  }

  @Test
  void fromValues_throwException() {
    // WHEN, THEN
    assertThrows(UnsupportedOperationException.class,
        () -> IEcommerce.fromValues(List.of("value")));
  }
}
