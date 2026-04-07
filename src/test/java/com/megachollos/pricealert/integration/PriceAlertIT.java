package com.megachollos.pricealert.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.pricealert.infrastructure.jpa.entities.PriceAlertEntity;
import com.megachollos.pricealert.infrastructure.jpa.repositories.JpaPriceAlertRepository;
import com.megachollos.pricealert.infrastructure.rest.dtos.PriceAlertDtoOut;
import com.megachollos.testUtils.bases.BaseIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class PriceAlertIT extends BaseIntegrationTest {

  public static final String PATH = "/price-alerts/";

  @Autowired
  private JpaPriceAlertRepository jpaPriceAlertRepository;

  @Test
  void shouldCreatePriceAlert() {
    given()
        .port(localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body("""
            {
              "productReference": "134029164",
              "ecommerce": "mediamarkt",
              "targetPrice": 59.99,
              "email": "student@test.com"
            }
            """)
        .when()
        .post(PATH)
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void shouldReturnPersistedPriceAlerts() {
    jpaPriceAlertRepository.save(PriceAlertEntity.builder()
        .productReference("134029164")
        .ecommerce("mediamarkt")
        .targetPrice(new BigDecimal("59.99"))
        .email("student@test.com")
        .createdAt(LocalDateTime.now())
        .build());

    List<PriceAlertDtoOut> priceAlerts = given()
        .port(localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get(PATH)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .as(new TypeRef<>() {});

    assertThat(priceAlerts).hasSize(1);
    assertThat(priceAlerts.get(0).getEmail()).isEqualTo("student@test.com");
  }
}
