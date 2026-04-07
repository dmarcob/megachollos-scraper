package com.megachollos.product.application;

import static com.megachollos.testUtils.providers.ProductProvider.getProduct;
import static com.megachollos.testUtils.providers.SearchProvider.getSearchCriteria;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.megachollos.product.domain.Product;
import com.megachollos.product.domain.ProductRepository;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class SearchProductsTest extends BaseUnitTest {

  @InjectMocks
  private SearchProducts searchProducts;

  @Mock
  private ProductRepository productRepository;

  @Test
  void search_OK() {
    // GIVEN
    when(productRepository.scroll(any()))
        .thenReturn(SearchResult.<Product>builder().elements(List.of(getProduct())).build());

    // WHEN
    SearchResult<Product> result = searchProducts.search(getSearchCriteria());

    // THEN
    assertThat(result).isNotNull();
    assertThat(result.getElements()).hasSize(1);
  }
}