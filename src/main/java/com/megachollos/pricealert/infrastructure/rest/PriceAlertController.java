package com.megachollos.pricealert.infrastructure.rest;

import com.megachollos.pricealert.infrastructure.jpa.entities.PriceAlertEntity;
import com.megachollos.pricealert.infrastructure.jpa.repositories.JpaPriceAlertRepository;
import com.megachollos.pricealert.infrastructure.rest.dtos.PriceAlertDtoIn;
import com.megachollos.pricealert.infrastructure.rest.dtos.PriceAlertDtoOut;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/price-alerts")
@RequiredArgsConstructor
@Slf4j
public class PriceAlertController {

  private final JpaPriceAlertRepository jpaPriceAlertRepository;

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public void createPriceAlert(@RequestBody PriceAlertDtoIn dto) {
    log.info("Received request to create price alert for email: {}", dto.getEmail());
    jpaPriceAlertRepository.save(PriceAlertEntity.builder()
        .productReference(dto.getProductReference())
        .ecommerce(dto.getEcommerce())
        .targetPrice(dto.getTargetPrice())
        .email(dto.getEmail())
        .createdAt(LocalDateTime.now())
        .build());
  }

  @GetMapping("/")
  public List<PriceAlertDtoOut> getPriceAlerts() {
    log.info("Received request to get price alerts");
    return jpaPriceAlertRepository.findAll().stream()
        .map(e -> PriceAlertDtoOut.builder()
            .id(e.getId())
            .productReference(e.getProductReference())
            .ecommerce(e.getEcommerce())
            .targetPrice(e.getTargetPrice())
            .email(e.getEmail())
            .createdAt(e.getCreatedAt())
            .build())
        .toList();
  }
}
