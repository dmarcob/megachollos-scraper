package com.megachollos.testUtils.providers;

import com.megachollos.category.domain.Category;
import com.megachollos.category.jpa.entities.CategoryEntity;

public class CategoryProvider {

  public static final String UNIQUE_NAME = "electronica";
  public static final String DISPLAY_NAME = "Electronica";

  public static Category getCategory() {
    return Category.builder()
        .uniqueName(UNIQUE_NAME)
        .displayName(DISPLAY_NAME)
        .parentCategory(null)
        .build();
  }

  public static CategoryEntity getCategoryEntity() {
    return CategoryEntity.builder()
        .uniqueName(UNIQUE_NAME)
        .displayName(DISPLAY_NAME)
        .parentCategory(null)
        .build();
  }
}
