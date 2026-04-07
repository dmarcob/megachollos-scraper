package com.megachollos.category.domain.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryError implements GenericError {
  CATEGORY_NOT_EXIST("Category with uniqueName %s does not exist");

  private final String description;

  public String getCode() {
    return this.name();
  }

}