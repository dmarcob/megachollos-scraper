package com.megachollos.shared.domain.exceptions;


import com.megachollos.shared.domain.exceptions.errors.GenericError;

public class UnavailableException extends GenericException {

  public UnavailableException(GenericError error) {
    super(error);
  }

  public UnavailableException(GenericError error, Object... args) {
    super(error, args);
  }

}