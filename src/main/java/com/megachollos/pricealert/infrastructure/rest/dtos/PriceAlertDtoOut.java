package com.megachollos.pricealert.infrastructure.rest.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceAlertDtoOut {

  private Long id;
  private String productReference;
  private String ecommerce;
  private BigDecimal targetPrice;
  private String email;
  private LocalDateTime createdAt;
}
