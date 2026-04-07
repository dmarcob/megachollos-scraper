package com.megachollos.pricealert.infrastructure.rest.dtos;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PriceAlertDtoIn {

  private String productReference;
  private String ecommerce;
  private BigDecimal targetPrice;
  private String email;
}
