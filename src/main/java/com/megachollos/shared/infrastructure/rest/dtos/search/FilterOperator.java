package com.megachollos.shared.infrastructure.rest.dtos.search;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FilterOperator {
  CONTAINS("contains", true),
  NOT_CONTAINS("notContains", true),
  STARTS_WITH("startsWith", true),
  ENDS_WITH("endsWith", true),
  IS_NULL("isNull", false),
  IS_NOT_NULL("isNotNull", false),
  IS_BLANK("isBlank", false),
  IS_NOT_BLANK("isNotBlank", false),
  EQUAL("equal", true),
  NOT_EQUAL("notEqual", true),
  GREATER_THAN("greaterThan", true),
  LESS_THAN("lessThan", true),
  GREATER_THAN_OR_EQUAL_TO("greaterThanOrEqualTo", true),
  LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo", true),
  REGEXP("regexp", true),
  QUERY_STRING("queryString", true);

  @JsonValue
  @Schema(description = "Operador de comparación para filtrado", example = "contains")
  private final String operator;
  private final boolean requiredValue;
}
