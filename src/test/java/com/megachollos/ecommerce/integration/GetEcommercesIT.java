package com.megachollos.ecommerce.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.ecommerce.domain.Ecommerce;
import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import com.megachollos.testUtils.bases.BaseIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
public class GetEcommercesIT extends BaseIntegrationTest {

  public static final String PATH = "/ecommerces/";

  @Test
  @SneakyThrows
  void getEcommerces_OK() {

    // WHEN
    MasterDataListDtoOut result = given()
        .port(this.localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .when()
        .get(PATH)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .as(new TypeRef<>() {
        });

    // THEN
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isEqualTo(Ecommerce.values().length);
    assertThat(result.getElements()).hasSize(Ecommerce.values().length);
  }
}
