package com.megachollos.brand.infrastructure.rest.mappers;

import com.megachollos.brand.domain.Brand;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListElementDtoOut;
import java.util.List;
import java.util.Optional;

public class BrandMapper {

  public static MasterDataListDtoOut toMasterDataListDtoOut(List<Brand> brands) {
    return MasterDataListDtoOut.builder()
        .totalElements(Optional.ofNullable(brands).orElse(List.of()).size())
        .elements(Optional.ofNullable(brands).orElse(List.of()).stream().map(brand ->
                MasterDataListElementDtoOut.builder()
                    .id(brand.getUniqueName())
                    .value(brand.getDisplayName())
                    .build())
            .toList())
        .build();
  }
}
