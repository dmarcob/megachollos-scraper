package com.megachollos.shared.infrastructure.elastic.builders;

import static com.megachollos.shared.infrastructure.elastic.FieldType.DYNAMIC;
import static com.megachollos.testUtils.providers.SearchProvider.getSort;
import static org.assertj.core.api.Assertions.assertThat;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.megachollos.shared.infrastructure.rest.dtos.search.Sort;
import com.megachollos.shared.infrastructure.rest.dtos.search.SortOrder;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SortBuilderTest extends BaseUnitTest {

  public static Stream<Arguments> validArgumentProvider() {
    return Stream.of(
        Arguments.of("Empty sorts", List.of(), new SortBuilder(List.of())),
        Arguments.of("Null sorts", null, new SortBuilder(List.of())),
        Arguments.of("One sort ASC -> ASC", List.of(getSort().withSort(SortOrder.ASC)),
            new SortBuilder(List.of(
                getSortOptions(getSort().getField(),
                    co.elastic.clients.elasticsearch._types.SortOrder.Asc)))),
        Arguments.of("One sort DESC -> DESC", List.of(getSort().withSort(SortOrder.DESC)),
            new SortBuilder(List.of(
                getSortOptions(getSort().getField(),
                    co.elastic.clients.elasticsearch._types.SortOrder.Desc)))),
        Arguments.of("One sort without ASC/DESC -> ASC", List.of(getSort().withSort(null)),
            new SortBuilder(List.of(
                getSortOptions(getSort().getField(),
                    co.elastic.clients.elasticsearch._types.SortOrder.Asc)))),
        Arguments.of("Multiple sorts", List.of(
                getSort().withSort(SortOrder.ASC),
                getSort().withField(getSort().getField() + "2").withSort(SortOrder.DESC),
                getSort().withField(getSort().getField() + "3").withSort(SortOrder.ASC)
            ),
            new SortBuilder(List.of(
                getSortOptions(getSort().getField(),
                    co.elastic.clients.elasticsearch._types.SortOrder.Asc),
                getSortOptions(getSort().getField() + "2",
                    co.elastic.clients.elasticsearch._types.SortOrder.Desc),
                getSortOptions(getSort().getField() + "3",
                    co.elastic.clients.elasticsearch._types.SortOrder.Asc)
            ))
        ));
  }

  private static SortOptions getSortOptions(String field,
      co.elastic.clients.elasticsearch._types.SortOrder sort) {
    return SortOptions.of(s -> s
        .field(FieldSort.of(f -> f
            .field(DYNAMIC.withIndex(field))
            .order(sort)
            .missing("_last")
            .unmappedType(co.elastic.clients.elasticsearch._types.mapping.FieldType.Keyword)
        )));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("validArgumentProvider")
  public void sortBuilderOf_OK(String testCase, List<Sort> sorts, SortBuilder expected) {
    // WHEN
    SortBuilder result = SortBuilder.of(sorts);

    // THEN
    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }
}