package com.megachollos.ecommerce.application;

import com.megachollos.ecommerce.domain.Ecommerce;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class GetEcommerces {

  public List<Ecommerce> get() {
    return Arrays.stream(Ecommerce.values())
        .sorted(Comparator.comparing(Ecommerce::getUniqueName))
        .toList();
  }
}
