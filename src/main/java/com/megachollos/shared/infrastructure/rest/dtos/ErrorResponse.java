package com.megachollos.shared.infrastructure.rest.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime timestamp;
  private String code;
  private String description;

  public ErrorResponse() {
    timestamp = LocalDateTime.now();
  }

  public ErrorResponse(String description, String code) {
    this();
    this.code = code;
    this.description = description;
  }
}
