package com.megachollos.testUtils.providers;

import com.megachollos.brand.domain.Brand;
import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;

public class BrandProvider {

  public static final String UNIQUE_NAME = "samsung";
  public static final String DISPLAY_NAME = "Samsung";

  public static Brand getBrand() {
    return Brand.builder()
        .uniqueName(UNIQUE_NAME)
        .displayName(DISPLAY_NAME)
        .build();
  }

  public static BrandEntity getBrandEntity() {
    return BrandEntity.builder()
        .uniqueName(UNIQUE_NAME)
        .displayName(DISPLAY_NAME)
        .build();
  }
}
