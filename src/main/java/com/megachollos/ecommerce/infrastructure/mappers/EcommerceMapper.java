package com.megachollos.ecommerce.infrastructure.mappers;

import com.megachollos.ecommerce.domain.Ecommerce;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListElementDtoOut;
import java.util.List;
import java.util.Optional;

public class EcommerceMapper {

  public static MasterDataListDtoOut toMasterDataListDtoOut(List<Ecommerce> brands) {
    return MasterDataListDtoOut.builder()
        .totalElements(Optional.ofNullable(brands).orElse(List.of()).size())
        .elements(Optional.ofNullable(brands).orElse(List.of()).stream().map(ecommerce ->
                MasterDataListElementDtoOut.builder()
                    .id(ecommerce.getUniqueName())
                    .value(ecommerce.getDisplayName())
                    .build())
            .toList())
        .build();
  }
}
