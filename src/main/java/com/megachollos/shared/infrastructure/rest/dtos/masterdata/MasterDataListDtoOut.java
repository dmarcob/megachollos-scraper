package com.megachollos.shared.infrastructure.rest.dtos.masterdata;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class MasterDataListDtoOut {

  private Integer totalElements;
  private List<MasterDataListElementDtoOut> elements;
}
