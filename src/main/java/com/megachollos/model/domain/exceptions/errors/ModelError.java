package com.megachollos.model.domain.exceptions.errors;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelError implements GenericError {
  MODEL_NO_RESULTS("Model %s has no results"),
  MODEL_NOT_EXIST("Model with id %s does not exist"),
  MODEL_INVALID_DISPLAY_NAME("DisplayName %s is not nvalid");

  private final String description;

  public String getCode() {
    return this.name();
  }

}