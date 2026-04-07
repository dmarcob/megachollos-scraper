package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.shared.infrastructure.rest.dtos.search.FilterOperator;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

class EqualityOperatorTest extends BaseUnitTest {

  private static final String FIELD = "field";
  private static final String VALUE = "value";

  @InjectMocks
  private EqualityOperator operator;

  private static Stream<Arguments> validArgumentProvider() {
    return Stream.of(
        Arguments.of(FilterOperator.EQUAL, 1, 0),
        Arguments.of(FilterOperator.NOT_EQUAL, 0, 1)
    );
  }

  @ParameterizedTest
  @MethodSource("validArgumentProvider")
  void apply_OK(FilterOperator filterOperator, Integer mustQuerySize, Integer mustNotQuerySize) {
    // GIVEN
    Filter filter = Filter.builder()
        .field(FIELD)
        .operator(filterOperator)
        .value(VALUE)
        .build();
    List<Query> expectedMustQuery = new ArrayList<>();
    List<Query> expectedMustNotQuery = new ArrayList<>();

    // WHEN
    assertDoesNotThrow(
        () -> operator.apply(expectedMustQuery, expectedMustNotQuery, filter));

    // THEN
    assertThat(expectedMustQuery).hasSize(mustQuerySize);
    assertThat(expectedMustNotQuery).hasSize(mustNotQuerySize);
  }

  @Test
  void apply_throwValidationException() {
    // GIVEN
    Filter filter = Filter.builder()
        .field(FIELD)
        .operator(FilterOperator.GREATER_THAN)
        .value(VALUE)
        .build();
    List<Query> expectedMustQuery = new ArrayList<>();
    List<Query> expectedMustNotQuery = new ArrayList<>();

    // WHEN
    BadRequestException exception = assertThrows(BadRequestException.class,
        () -> operator.apply(expectedMustQuery, expectedMustNotQuery, filter));
    assertThat(exception.getCode()).isEqualTo(UNSUPPORTED_FILTER_OPERATOR.getCode());
  }
}