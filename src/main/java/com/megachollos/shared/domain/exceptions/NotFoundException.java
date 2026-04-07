package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class NotFoundException extends GenericException {

  public NotFoundException(GenericError error) {
    super(error);
  }

  public NotFoundException(GenericError error, Object... args) {
    super(error, args);
  }

}