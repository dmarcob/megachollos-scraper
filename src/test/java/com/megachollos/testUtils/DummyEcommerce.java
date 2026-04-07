package com.megachollos.testUtils;

import com.megachollos.ecommerce.domain.IEcommerce;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DummyEcommerce implements IEcommerce {
  DUMMY("dummy", "dummy", true),
  DUMMY_INACTIVE("dummy-inactive", "dummy", false);

  private final String uniqueName;
  private final String displayName;
  private final boolean active;

  public static Optional<DummyEcommerce> fromValue(String uniqueName) {
    return Optional.of(DummyEcommerce.DUMMY);
  }
}
