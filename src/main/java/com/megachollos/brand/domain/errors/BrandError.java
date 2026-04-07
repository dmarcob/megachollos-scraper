package com.megachollos.brand.domain.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BrandError implements GenericError {
  BRAND_NOT_EXIST("Brand with uniqueName %s does not exist");

  private final String description;

  public String getCode() {
    return this.name();
  }

}