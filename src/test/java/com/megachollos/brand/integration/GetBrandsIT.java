package com.megachollos.brand.integration;

import static com.megachollos.testUtils.providers.BrandProvider.getBrandEntity;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.shared.infrastructure.rest.dtos.masterdata.MasterDataListDtoOut;
import com.megachollos.testUtils.bases.BaseIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class GetBrandsIT extends BaseIntegrationTest {

  public static final String PATH = "/brands/";

  @Test
  @SneakyThrows
  void getBrands_OK() {
    // GIVEN
    jpaBrandRepository.save(getBrandEntity());

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
    assertThat(result.getTotalElements()).isEqualTo(1);
    assertThat(result.getElements()).hasSize(1);
    assertThat(result.getElements().get(0).getId()).isEqualTo(getBrandEntity().getUniqueName());
    assertThat(result.getElements().get(0).getValue()).isEqualTo(getBrandEntity().getDisplayName());
  }
}
