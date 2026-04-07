package com.megachollos.ecommerce.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.ecommerce.domain.Ecommerce;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class GetEcommercesTest extends BaseUnitTest {

  @InjectMocks
  private GetEcommerces getEcommerces;


  @Test
  void get_OK() {
    // WHEN
    List<Ecommerce> result = getEcommerces.get();

    // THEN
    assertThat(result).isNotNull();
  }
}
