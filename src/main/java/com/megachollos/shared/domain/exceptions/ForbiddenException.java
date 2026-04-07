package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class ForbiddenException extends GenericException {

  public ForbiddenException(GenericError error) {
    super(error);
  }

  public ForbiddenException(GenericError error, Object... args) {
    super(error, args);
  }
}