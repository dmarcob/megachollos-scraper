package com.megachollos.shared.domain.exceptions.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SharedError implements GenericError {
  ERROR("Ha ocurrido un error inesperado"),
  INVALID_PARAMS("Existen parámetros que no cumplen las validaciones establecidas"),
  MISSING_PATH_PARAM("Existe un path param obligatorio que no se ha informado"),
  MISSING_REQUEST_PARAM("Existe un reqeuest param obligatorio que no se ha informado"),
  RESOURCE_PATH_NOT_FOUND("La ruta a la que intenta acceder '%s' no existe"),
  UNSUPPORTED_FILTER_OPERATOR("The operator '%s' is not available for filtered field type"),
  UNSUPPORTED_STAT_OPERATOR("The operator '%s' is not available for stats field type"),
  MISSING_FILTER_VALUE("The '%s' filter value is required for operator '%s'"),
  MULTIPLE_FILTER_VALUE("Only one of 'value' or 'values' must be provided for filter '%s'"),
  CLASS_NOT_EXIST("Class '%s' does not exist"),
  CLASS_NOT_INHERIT_FROM_WEB_DRIVER("Class '%s' does not inherit from WebDriver class");

  private final String description;

  public String getCode() {
    return this.name();
  }

}