package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class BadRequestException extends GenericException {

  public BadRequestException(GenericError error) {
    super(error);
  }

  public BadRequestException(GenericError error, Object... args) {
    super(error, args);
  }
}