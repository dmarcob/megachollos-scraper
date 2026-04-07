package com.megachollos.shared.infrastructure.rest.dtos.stats.statresult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class StatResult {

  private String field;
}
