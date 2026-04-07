package com.megachollos.product.domain;

import static com.megachollos.product.domain.exceptions.errors.ProductError.PRODUCT_STATE_NOT_EXIST;

import com.megachollos.shared.domain.exceptions.InternalException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public enum ProductState {
  /**
   * Product is valid
   */
  OK("ok"),

  /**
   * Product is invalid
   */
  ERROR("error");

  private final String value;


  public static ProductState fromValue(String value) {
    for (ProductState state : ProductState.values()) {
      if (state.getValue().equals(value)) {
        return state;
      }
    }
    log.warn("ProductState {} does not exist", value);
    throw new InternalException(PRODUCT_STATE_NOT_EXIST, value);
  }
}
