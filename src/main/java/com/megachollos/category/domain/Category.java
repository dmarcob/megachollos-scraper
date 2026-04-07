package com.megachollos.category.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category {

  private String uniqueName;
  private String displayName;
  private Category parentCategory;
}
