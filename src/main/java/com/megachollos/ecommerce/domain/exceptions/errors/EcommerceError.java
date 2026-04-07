package com.megachollos.ecommerce.domain.exceptions.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EcommerceError implements GenericError {
  ECOMMERCE_NOT_EXIST("Ecommerce %s does not exist");

  private final String description;

  public String getCode() {
    return this.name();
  }

}