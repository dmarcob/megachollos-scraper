package com.megachollos.product.infrastructure.elastic;

import static com.megachollos.brand.domain.errors.BrandError.BRAND_NOT_EXIST;
import static com.megachollos.model.domain.exceptions.errors.ModelError.MODEL_NOT_EXIST;
import static com.megachollos.testUtils.providers.BrandProvider.getBrand;
import static com.megachollos.testUtils.providers.ModelProvider.getModel;
import static com.megachollos.testUtils.providers.ProductProvider.getProduct;
import static com.megachollos.testUtils.providers.ProductProvider.getProductDocument;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.product.domain.Product;
import com.megachollos.shared.domain.exceptions.InternalException;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ProductDocumentTest extends BaseUnitTest {

  public static Stream<Arguments> toDomainArgumentProvider() {
    return Stream.of(
        Arguments.of("Happy path",
            getProductDocument(),
            getProduct()
        ),
        Arguments.of("Null objects",
            getProductDocument().withBrand(null).withModel(null),
            getProduct().withBrand(null).withModel(null)
        )
    );
  }

  public static Stream<Arguments> fromDomainArgumentProvider() {
    return Stream.of(
        Arguments.of("Happy path",
            getProduct(),
            getProductDocument()
        ),
        Arguments.of("Null objects",
            getProduct().withEcommerce(null).withState(null).withModel(null)
                .withBrand(null),
            getProductDocument().withEcommerce(null).withState(null).withModel(null)
                .withBrand(null)
        )
    );
  }


  @ParameterizedTest(name = "{0}")
  @MethodSource("toDomainArgumentProvider")
  void toDomain_OK(String testCase, ProductDocument given, Product expected) {
    // GIVEN
    ModelRepository modelRepository = mock(ModelRepository.class);
    BrandRepository brandRepository = mock(BrandRepository.class);
    lenient().when(modelRepository.findById(any())).thenReturn(Optional.of(getModel()));
    lenient().when(brandRepository.findByUniqueName(any())).thenReturn(Optional.of(getBrand()));

    // WHEN
    Product result = given.toDomain(modelRepository, brandRepository);

    // THEN
    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void toDomain_ecommerceDoesNotExist_throwException() {
    // GIVEN
    ModelRepository modelRepository = mock(ModelRepository.class);
    BrandRepository brandRepository = mock(BrandRepository.class);
    lenient().when(modelRepository.findById(any())).thenReturn(Optional.of(getModel()));
    lenient().when(brandRepository.findByUniqueName(any())).thenReturn(Optional.of(getBrand()));
    ProductDocument productDocument = getProductDocument().withEcommerce("INVALID");

    // WHEN, THEN
    assertThrows(InternalException.class,
        () -> productDocument.toDomain(modelRepository, brandRepository));
  }

  @Test
  void toDomain_stateDoesNotExist_throwException() {
    // GIVEN
    ModelRepository modelRepository = mock(ModelRepository.class);
    BrandRepository brandRepository = mock(BrandRepository.class);
    lenient().when(modelRepository.findById(any())).thenReturn(Optional.of(getModel()));
    lenient().when(brandRepository.findByUniqueName(any())).thenReturn(Optional.of(getBrand()));
    ProductDocument productDocument = getProductDocument().withState("INVALID");

    // WHEN, THEN
    assertThrows(InternalException.class,
        () -> productDocument.toDomain(modelRepository, brandRepository));
  }

  @Test
  void toDomain_modelDoesNotExist_throwException() {
    // GIVEN
    ModelRepository modelRepository = mock(ModelRepository.class);
    BrandRepository brandRepository = mock(BrandRepository.class);
    lenient().when(modelRepository.findById(any())).thenReturn(Optional.empty());
    lenient().when(brandRepository.findByUniqueName(any())).thenReturn(Optional.of(getBrand()));
    ProductDocument productDocument = getProductDocument();

    // WHEN
    InternalException exception = assertThrows(InternalException.class,
        () -> productDocument.toDomain(modelRepository, brandRepository));

    // THEN
    assertThat(exception.getCode()).isEqualTo(MODEL_NOT_EXIST.getCode());
  }

  @Test
  void toDomain_brandDoesNotExist_throwException() {
    // GIVEN
    ModelRepository modelRepository = mock(ModelRepository.class);
    BrandRepository brandRepository = mock(BrandRepository.class);
    lenient().when(modelRepository.findById(any())).thenReturn(Optional.of(getModel()));
    lenient().when(brandRepository.findByUniqueName(any())).thenReturn(Optional.empty());
    ProductDocument productDocument = getProductDocument();

    // WHEN
    InternalException exception = assertThrows(InternalException.class,
        () -> productDocument.toDomain(modelRepository, brandRepository));

    // THEN
    assertThat(exception.getCode()).isEqualTo(BRAND_NOT_EXIST.getCode());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("fromDomainArgumentProvider")
  void fromDomain_OK(String testCase, Product given, ProductDocument expected) {
    // WHEN
    ProductDocument result = ProductDocument.fromDomain(given);

    // THEN
    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }
}
