package com.megachollos.product.domain;

import static com.megachollos.product.domain.exceptions.errors.ProductError.PRODUCT_ERROR_REASON_NOT_EXIST;

import com.megachollos.shared.domain.exceptions.InternalException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public enum ProductErrorReason {
  NO_RESULTS("no-results"),
  MISSING_REFERENCE("missing-reference"),
  MISSING_TITLE("missing-title"),
  MISSING_PRICE("missing-price"),
  MISSING_ORIGINAL_PRICE("missing-original-price"),
  MISSING_DISCOUNT("missing-discount"),
  MISSING_URL("missing-url"),
  ZERO_DISCOUNT("zero-discount");

  private final String value;


  public static ProductErrorReason fromValue(String value) {
    for (ProductErrorReason state : ProductErrorReason.values()) {
      if (state.getValue().equals(value)) {
        return state;
      }
    }
    log.warn("ProductErrorReason {} does not exist", value);
    throw new InternalException(PRODUCT_ERROR_REASON_NOT_EXIST, value);
  }
}
