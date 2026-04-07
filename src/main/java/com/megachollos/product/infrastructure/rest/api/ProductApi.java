package com.megachollos.product.infrastructure.rest.api;

import com.megachollos.product.infrastructure.rest.dtos.ProductDtoOut;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ProductApi {

  @Operation(description = "Búsqueda de productos con filtros y ordenaciones")
  @ApiResponse(
      responseCode = "200",
      description = "Búsqueda realizada exitosamente"
  )
  @PostMapping(value = "/search")
  @ResponseStatus(HttpStatus.OK)
  SearchResult<ProductDtoOut> search(@RequestBody @Valid SearchCriteria searchCriteria);

  @Operation(description = "Recupera los valores de los filtros")
  @ApiResponse(
      responseCode = "200",
      description = "Obtención de estadísticas realizada exitosamente"
  )
  @PostMapping(value = "/stats")
  @ResponseStatus(HttpStatus.OK)
  StatsResult stats(@RequestBody @Valid StatsCriteria statsCriteria);
}
