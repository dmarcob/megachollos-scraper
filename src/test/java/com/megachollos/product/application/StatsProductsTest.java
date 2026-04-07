package com.megachollos.product.application;

import static com.megachollos.testUtils.providers.StatsProvider.getStatsCriteria;
import static com.megachollos.testUtils.providers.StatsProvider.getStatsResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.megachollos.product.domain.ProductRepository;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import com.megachollos.testUtils.bases.BaseUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class StatsProductsTest extends BaseUnitTest {

  @InjectMocks
  private StatsProducts statsProducts;

  @Mock
  private ProductRepository productRepository;

  @Test
  void stats_OK() {
    // GIVEN
    when(productRepository.stats(any())).thenReturn(getStatsResult());

    // WHEN
    StatsResult result = statsProducts.stats(getStatsCriteria());

    // THEN
    assertThat(result).isNotNull();
  }
}