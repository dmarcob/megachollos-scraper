package com.megachollos.brand.infrastructure.rest;

import com.megachollos.brand.application.GetBrands;
import com.megachollos.brand.infrastructure.rest.api.BrandApi;
import com.megachollos.brand.infrastructure.rest.mappers.BrandMapper;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
@Slf4j
public class BrandController implements BrandApi {

  private final GetBrands getBrands;

  @Override
  @Cacheable("brands")
  public MasterDataListDtoOut getBrands() {
    log.info("Received request to get brands");
    return BrandMapper.toMasterDataListDtoOut(getBrands.get());
  }
}
