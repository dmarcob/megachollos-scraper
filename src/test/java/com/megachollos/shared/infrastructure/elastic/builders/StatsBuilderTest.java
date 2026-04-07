package com.megachollos.shared.infrastructure.elastic.builders;

import static com.megachollos.testUtils.providers.SearchProvider.getFilter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class StatsBuilderTest extends BaseUnitTest {

  public static Stream<Arguments> validArgumentProvider() {
    return Stream.of(
        Arguments.of("Empty filters", List.of()),
        Arguments.of("Null filters", null),
        Arguments.of("Happy path", List.of(getFilter()))
    );
  }

  public static Stream<Arguments> invalidArgumentProvider() {
    return Stream.of(
        Arguments.of("value and values missing",
            List.of(getFilter().withValue(null).withValues(null))),
        Arguments.of("value and values informed", List.of(
            getFilter().withValue("value").withValues(List.of("value"))
                .withValues(List.of("value"))))
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("validArgumentProvider")
  public void toQuery_OK(String testCase, List<Filter> filters) {
    // WHEN, THEN
    assertDoesNotThrow(() -> {
      Query query = FilterBuilder.toQuery(filters);
      assertThat(query).isNotNull();
    });
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidArgumentProvider")
  public void toQuery_KO(String testsCase, List<Filter> filters) {
    // WHEN, THEN
    assertThrows(BadRequestException.class, () -> FilterBuilder.toQuery(filters));
  }
}