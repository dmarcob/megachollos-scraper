package com.megachollos.ecommerce.infrastructure;

import com.megachollos.ecommerce.application.GetEcommerces;
import com.megachollos.ecommerce.infrastructure.api.EcommerceApi;
import com.megachollos.ecommerce.infrastructure.mappers.EcommerceMapper;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ecommerces")
@RequiredArgsConstructor
@Slf4j
public class EcommerceController implements EcommerceApi {

  private final GetEcommerces getEcommerces;

  @Override
  @Cacheable("ecommerces")
  public MasterDataListDtoOut getEcommerces() {
    log.info("Received request to get brands");
    return EcommerceMapper.toMasterDataListDtoOut(getEcommerces.get());
  }
}
