package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class ConflictException extends GenericException {

  public ConflictException(GenericError error) {
    super(error);
  }

  public ConflictException(GenericError error, Object... args) {
    super(error, args);
  }

}