package com.megachollos.product.application;

import com.megachollos.product.domain.ProductRepository;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsProducts {

  private final ProductRepository productRepository;

  public StatsResult stats(StatsCriteria statsCriteria) {
    return productRepository.stats(statsCriteria);
  }
}
