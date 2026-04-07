package com.megachollos.ecommerce.domain;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public enum Ecommerce implements IEcommerce {
  // -----------------------------------
  // ELECTRÓNICA
  // -----------------------------------
  MEDIAMARKT("mediamarkt", "MediaMarkt", true),
  KEYCHRON("keychron", "Keychron", true),

  // -----------------------------------
  // ALIMENTACIÓN
  // -----------------------------------
  HSN("hsn", "HSN", true),
  DIA("dia", "DIA", true),

  // -----------------------------------
  // PROTEGIDOS - Actualmente inactivos
  // -----------------------------------
  FNAC("fnac", "fnac", false);

  private final String uniqueName;
  private final String displayName;
  private final boolean active;

  public static Optional<Ecommerce> fromValue(String uniqueName) {
    for (Ecommerce ecommerce : Ecommerce.values()) {
      if (ecommerce.getUniqueName().equals(uniqueName)) {
        return Optional.of(ecommerce);
      }
    }
    log.warn("Ecommerce {} does not exist", uniqueName);
    return Optional.empty();
  }

  public static List<Ecommerce> fromValues(List<String> uniqueNames) {
    return Optional.ofNullable(uniqueNames).orElse(List.of()).stream()
        .map(Ecommerce::fromValue)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }
}
