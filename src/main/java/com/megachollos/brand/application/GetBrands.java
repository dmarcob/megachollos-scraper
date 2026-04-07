package com.megachollos.brand.application;

import com.megachollos.brand.domain.Brand;
import com.megachollos.brand.domain.BrandRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class GetBrands {

  private final BrandRepository brandRepository;

  public List<Brand> get() {
    return brandRepository.findAll();
  }
}
