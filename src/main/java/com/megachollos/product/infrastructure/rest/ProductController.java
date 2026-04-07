package com.megachollos.product.infrastructure.rest;

import com.megachollos.product.application.SearchProducts;
import com.megachollos.product.application.StatsProducts;
import com.megachollos.product.infrastructure.rest.api.ProductApi;
import com.megachollos.product.infrastructure.rest.dtos.ProductDtoOut;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductApi {

  private final SearchProducts searchProducts;
  private final StatsProducts statsProducts;

  @Override
  public SearchResult<ProductDtoOut> search(SearchCriteria searchCriteria) {
    log.info("Received request to search products: [{}]", searchCriteria);
    return searchProducts.search(searchCriteria).map(ProductDtoOut::fromDomain);
  }

  @Override
  public StatsResult stats(StatsCriteria statsCriteria) {
    log.info("Received request to get stats: [{}]", statsCriteria);
    return statsProducts.stats(statsCriteria);
  }
}
