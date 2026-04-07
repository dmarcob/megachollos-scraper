package com.megachollos.ecommerce.domain;

import static com.megachollos.ecommerce.domain.Ecommerce.MEDIAMARKT;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class EcommerceTest extends BaseUnitTest {

  @Test
  void fromValue_OK() {
    // WHEN
    Optional<Ecommerce> result = Ecommerce.fromValue(MEDIAMARKT.getUniqueName());

    // THEN
    assertThat(result).isPresent().get().isEqualTo(MEDIAMARKT);
  }

  @Test
  void fromValue_noExist_returnNull() {
    // WHEN
    Optional<Ecommerce> result = Ecommerce.fromValue("NO_EXIST");

    // THEN
    assertThat(result).isEmpty();
  }
}
