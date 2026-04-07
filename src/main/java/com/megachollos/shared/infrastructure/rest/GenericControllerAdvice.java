package com.megachollos.shared.infrastructure.rest;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.ERROR;
import static com.megachollos.shared.domain.exceptions.errors.SharedError.INVALID_PARAMS;
import static com.megachollos.shared.domain.exceptions.errors.SharedError.MISSING_PATH_PARAM;
import static com.megachollos.shared.domain.exceptions.errors.SharedError.MISSING_REQUEST_PARAM;
import static com.megachollos.shared.domain.exceptions.errors.SharedError.RESOURCE_PATH_NOT_FOUND;

import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.domain.exceptions.ConflictException;
import com.megachollos.shared.domain.exceptions.ForbiddenException;
import com.megachollos.shared.domain.exceptions.GenericException;
import com.megachollos.shared.domain.exceptions.InternalException;
import com.megachollos.shared.domain.exceptions.NotFoundException;
import com.megachollos.shared.domain.exceptions.UnavailableException;
import com.megachollos.shared.infrastructure.rest.dtos.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GenericControllerAdvice {

  @ApiResponse(
      description = "Bad Request",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler(BadRequestException.class)
  protected ResponseEntity<ErrorResponse> handleException(BadRequestException ex) {
    return buildResponse(ex, HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Conflict",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "409"
  )
  @ExceptionHandler(ConflictException.class)
  protected ResponseEntity<ErrorResponse> handleException(ConflictException ex) {
    return buildResponse(ex, HttpStatus.CONFLICT);
  }

  @ApiResponse(
      description = "Forbidden",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "403"
  )
  @ExceptionHandler({ForbiddenException.class})
  protected ResponseEntity<ErrorResponse> handleException(ForbiddenException ex) {
    return this.buildResponse(ex, HttpStatus.FORBIDDEN);
  }

  @ApiResponse(
      description = "Internal Server Error",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "500"
  )
  @ExceptionHandler(InternalException.class)
  protected ResponseEntity<ErrorResponse> handleException(InternalException ex) {
    return buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ApiResponse(
      description = "Internal Server Error",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "500"
  )
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error("Se ha producido un error {}", ex.getMessage());
    ErrorResponse response = new ErrorResponse(ex.getMessage(), ERROR.getCode());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ApiResponse(
      description = "Not Found",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "404"
  )
  @ExceptionHandler(NotFoundException.class)
  protected ResponseEntity<ErrorResponse> handleException(NotFoundException ex) {
    return buildResponse(ex, HttpStatus.NOT_FOUND);
  }

  @ApiResponse(
      description = "Unavailable Service Error",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "502"
  )
  @ExceptionHandler(UnavailableException.class)
  protected ResponseEntity<ErrorResponse> handleException(UnavailableException ex) {
    return buildResponse(ex, HttpStatus.BAD_GATEWAY);
  }

  @ApiResponse(
      description = "Argumentos no válidos",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
    log.warn("MethodArgumentNotValidException: {}", e.getMessage());
    return buildResponse(new GenericException(INVALID_PARAMS), HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Formato inválido",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler(HandlerMethodValidationException.class)
  protected ResponseEntity<ErrorResponse> handleException(HandlerMethodValidationException e) {
    log.warn("HandlerMethodValidationException: {}", e.getMessage());
    return buildResponse(new GenericException(INVALID_PARAMS), HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Fallo en el tipaje de parámetros",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {
    log.warn("MethodArgumentTypeMismatchException: {}", e.getMessage());
    return buildResponse(new GenericException(INVALID_PARAMS), HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Path param obligatorio",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler({MissingPathVariableException.class})
  protected ResponseEntity<ErrorResponse> handleException(MissingPathVariableException e) {
    log.warn("MissingPathVariableException: {}", e.getMessage());
    return buildResponse(new GenericException(MISSING_PATH_PARAM), HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Request param obligatorio",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "400"
  )
  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<ErrorResponse> handleException(
      MissingServletRequestParameterException e) {
    log.warn("MissingServletRequestParameterException: {}", e.getMessage());
    return buildResponse(new GenericException(MISSING_REQUEST_PARAM), HttpStatus.BAD_REQUEST);
  }

  @ApiResponse(
      description = "Ruta no definida",
      content = {
          @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class))},
      responseCode = "404"
  )
  @ExceptionHandler(NoResourceFoundException.class)
  protected ResponseEntity<ErrorResponse> handleException(NoResourceFoundException e) {
    log.warn("NoResourceFoundException: {}", e.getMessage());
    NotFoundException newEx = new NotFoundException(RESOURCE_PATH_NOT_FOUND, e.getResourcePath());
    return buildResponse(newEx, HttpStatus.NOT_FOUND);
  }

  @ApiResponse(
      description = "Argumentos no válidos",
      content = {@Content(
          mediaType = "application/json",
          schema = @Schema(
              implementation = ErrorResponse.class
          )
      )},
      responseCode = "400"
  )
  @ExceptionHandler({ConstraintViolationException.class})
  protected ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
    log.warn("ConstraintViolationException: {}", e.getMessage());
    return buildResponse(new GenericException(INVALID_PARAMS), HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<ErrorResponse> buildResponse(GenericException ex, HttpStatus status) {
    ErrorResponse response = new ErrorResponse(ex.getMessage(), ex.getCode());
    return buildResponseEntity(response, status);
  }

  protected ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse response,
      HttpStatus status) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return new ResponseEntity<>(response, headers, status);
  }
}
