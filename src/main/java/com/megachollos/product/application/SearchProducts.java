package com.megachollos.product.application;

import com.megachollos.product.domain.Product;
import com.megachollos.product.domain.ProductRepository;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchProducts {

  private final ProductRepository productRepository;

  public SearchResult<Product> search(SearchCriteria searchCriteria) {
    return productRepository.scroll(searchCriteria);
  }
}
