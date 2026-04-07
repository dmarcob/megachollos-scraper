package com.megachollos.testUtils.providers;

import static com.megachollos.testUtils.providers.BrandProvider.getBrand;
import static com.megachollos.testUtils.providers.CategoryProvider.getCategory;

import com.megachollos.model.domain.Model;
import com.megachollos.model.jpa.entities.ModelEntity;

public class ModelProvider {

  public static final Long ID = 1L;
  public static final String DISPLAY_NAME = "Tarjeta Micro SD";

  public static Model getModel() {
    return Model.builder()
        .id(ID)
        .displayName(DISPLAY_NAME)
        .category(getCategory())
        .brand(getBrand())
        .build();
  }

  public static ModelEntity getModelEntity() {
    return ModelEntity.builder()
        .displayName(DISPLAY_NAME)
        .categoryId(CategoryProvider.UNIQUE_NAME)
        .brandId(BrandProvider.UNIQUE_NAME)
        .build();
  }
}
