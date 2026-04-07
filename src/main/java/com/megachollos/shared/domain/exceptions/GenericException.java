package com.megachollos.shared.domain.exceptions;

import com.megachollos.shared.domain.exceptions.errors.GenericError;
import lombok.Getter;

/**
 * Base exception
 */
@Getter
public class GenericException extends RuntimeException {

  private final String code;
  private final Object[] args;

  public GenericException(GenericError error) {
    super(error.getDescription());
    this.code = error.getCode();
    this.args = null;
  }

  public GenericException(GenericError error, Object... args) {
    super(getErrorDescription(error.getDescription(), args));
    this.code = error.getCode();
    this.args = args;
  }

  public static String getErrorDescription(String format, Object... args) {
    if (format == null || args == null || args.length == 0) {
      return format;
    }
    return String.format(format, args);
  }
}