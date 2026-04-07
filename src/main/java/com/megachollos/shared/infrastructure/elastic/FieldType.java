package com.megachollos.shared.infrastructure.elastic;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Elastic allows to index a field in different ways, using fields
 */
@Getter
@AllArgsConstructor
public enum FieldType {

  /**
   * Text index. (Ej: createdAt.keyword)
   * <p>
   * Used for text operations like CONTAINS, EQUAL, etc.
   */
  KEYWORD("keyword"),

  /**
   * Dynamic index. Inferred by elastic. (date, boolean, etc) (Ej: createdAt.dynamic)
   * <p>
   * Used for ordering and numeric, boolean and date operations
   */
  DYNAMIC("dynamic");

  private final String index;

  public String withIndex(String field) {
    return field + "." + this.index;
  }
}
