package com.megachollos.product.domain;

import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import java.util.List;
import java.util.function.Predicate;

public interface ProductRepository {

  boolean exists(Product product);

  void save(List<Product> products);

  void save(List<Product> products, List<Predicate<Product>> conditions);

  SearchResult<Product> scroll(SearchCriteria searchCriteria);

  StatsResult stats(StatsCriteria statsCriteria);
}
