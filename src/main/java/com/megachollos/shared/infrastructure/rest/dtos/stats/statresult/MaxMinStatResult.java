package com.megachollos.shared.infrastructure.rest.dtos.stats.statresult;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import lombok.experimental.SuperBuilder;

@Data
@With
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MaxMinStatResult extends StatResult {

  private BigDecimal min;
  private BigDecimal max;
  private BigDecimal average;
}
