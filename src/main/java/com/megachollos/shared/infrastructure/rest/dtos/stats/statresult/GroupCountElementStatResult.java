package com.megachollos.shared.infrastructure.rest.dtos.stats.statresult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import lombok.experimental.SuperBuilder;

@Data
@With
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GroupCountElementStatResult {

  private String value;
  private Long count;
}
