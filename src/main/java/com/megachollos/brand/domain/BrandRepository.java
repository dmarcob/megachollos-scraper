package com.megachollos.brand.domain;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {

  Optional<Brand> findByUniqueName(String uniqueName);

  List<Brand> findAll();
}
