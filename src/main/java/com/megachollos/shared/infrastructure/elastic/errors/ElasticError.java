package com.megachollos.shared.infrastructure.elastic.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElasticError implements GenericError {
  ELASTIC_NO_SUCH_INDEX("Index %s does not exist"),
  INVALID_QUERY("Invalid query: %s"),
  ELASTIC_UNAVAILABLE("Elastic is unavailable");

  private final String description;

  public String getCode() {
    return this.name();
  }

}