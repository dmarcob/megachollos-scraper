package com.megachollos.brand.application;

import static com.megachollos.testUtils.providers.BrandProvider.getBrand;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.megachollos.brand.domain.Brand;
import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetBrandsTest extends BaseUnitTest {

  @InjectMocks
  private GetBrands getBrands;

  @Mock
  private BrandRepository brandRepository;

  @Test
  void get_OK() {
    // GIVEN
    when(brandRepository.findAll()).thenReturn(List.of(getBrand()));

    // WHEN
    List<Brand> result = getBrands.get();

    // THEN
    assertThat(result).isNotNull().hasSize(1);
  }
}
