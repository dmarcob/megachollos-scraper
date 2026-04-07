package com.megachollos.shared.infrastructure.rest.dtos.masterdata;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class MasterDataListElementDtoOut {
  private String id;
  private String value;
}
