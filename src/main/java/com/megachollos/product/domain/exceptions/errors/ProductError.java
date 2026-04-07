package com.megachollos.product.domain.exceptions.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductError implements GenericError {
  PRODUCT_STATE_NOT_EXIST("Product State %s does not exist"),
  PRODUCT_ERROR_REASON_NOT_EXIST("Product Error Reason %s does not exist");

  private final String description;

  public String getCode() {
    return this.name();
  }

}