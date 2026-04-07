package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class InternalException extends GenericException {

  public InternalException(GenericError error) {
    super(error);
  }

  public InternalException(GenericError error, Object... args) {
    super(error, args);
  }

}