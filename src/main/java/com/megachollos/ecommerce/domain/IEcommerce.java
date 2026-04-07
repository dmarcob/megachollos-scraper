package com.megachollos.ecommerce.domain;

import java.util.List;
import java.util.Optional;

public interface IEcommerce {

  String getUniqueName();

  String getDisplayName();

  boolean isActive();

  static Optional<IEcommerce> fromValue(String uniqueName) {
    throw new UnsupportedOperationException("fromValue must be implemented");
  }

  static List<IEcommerce> fromValues(List<String> uniqueNames) {
    throw new UnsupportedOperationException("fromValues must be implemented");
  }
}
