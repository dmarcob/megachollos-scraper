package com.megachollos.ecommerce.infrastructure.api;

import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface EcommerceApi {

  @Operation(description = "Recupera el listado")
  @ApiResponse(
      responseCode = "200",
      description = "Obtención realizada exitosamente"
  )
  @GetMapping(value = "/")
  @ResponseStatus(HttpStatus.OK)
  MasterDataListDtoOut getEcommerces();
}
