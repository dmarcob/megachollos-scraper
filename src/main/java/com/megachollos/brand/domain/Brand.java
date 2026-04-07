package com.megachollos.brand.domain;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Brand {

  private String uniqueName;
  private String displayName;
}
