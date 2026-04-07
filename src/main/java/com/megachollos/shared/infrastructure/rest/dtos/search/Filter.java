package com.megachollos.shared.infrastructure.rest.dtos.search;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.MISSING_FILTER_VALUE;
import static com.megachollos.shared.domain.exceptions.errors.SharedError.MULTIPLE_FILTER_VALUE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.util.CollectionUtils;

@Data
@Builder
@With
public class Filter {

  @Schema(description = "Nombre del campo sobre el que aplicar el filtrado. Se permiten búsquedas en objetos anidados usando la notación punto (dot notation)", example = "translation.displayName")
  @NotNull
  @Size(min = 1)
  private String field;

  @Schema(description = "Valor del campo por el que se debe filtrar")
  private String value;

  @Schema(description = "Valores del campo por el que se debe filtrar")
  private List<String> values;

  @Schema(description = "Criterio de comparación para el filtrado", example = "contains")
  @NotNull
  private FilterOperator operator;

  /**
   * Checks if the value exists if required
   */
  @JsonIgnore
  public void validate() {
    if (operator != null && operator.isRequiredValue()) {
      if (value == null && CollectionUtils.isEmpty(values)) {
        throw new BadRequestException(MISSING_FILTER_VALUE, field, operator);
      }

      if (value != null && !CollectionUtils.isEmpty(values)) {
        throw new BadRequestException(MULTIPLE_FILTER_VALUE, field);
      }
    }
  }

  /**
   * Returns a list containing all the values associated with the filter. If a single value is
   * provided, a list containing that value is returned. If a list of values is provided, that list
   * is returned.
   */
  public List<String> allValues() {
    if (value != null) {
      return List.of(value);
    }

    if (!CollectionUtils.isEmpty(values)) {
      return values;
    }

    return List.of();

  }
}
