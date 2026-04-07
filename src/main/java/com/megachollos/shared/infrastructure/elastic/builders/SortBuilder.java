package com.megachollos.shared.infrastructure.elastic.builders;

import static com.megachollos.shared.infrastructure.elastic.FieldType.DYNAMIC;

import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import com.megachollos.shared.infrastructure.rest.dtos.search.Sort;
import com.megachollos.shared.infrastructure.rest.dtos.search.SortOrder;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
public class SortBuilder {

  private List<SortOptions> sortOptions;

  /**
   * Creates a new {@link SortBuilder} with {@code criteria} applied
   */
  public static SortBuilder of(@Nullable List<Sort> sorts) {
    List<SortOptions> sortOptions = Optional.ofNullable(sorts)
        .map(list -> list.stream()
            .map(sort -> SortOptions.of(s -> s
                .field(FieldSort.of(f -> f
                    .field(DYNAMIC.withIndex(sort.getField()))
                    .order(SortOrder.ASC == sort.getSort() ?
                        co.elastic.clients.elasticsearch._types.SortOrder.Asc :
                        co.elastic.clients.elasticsearch._types.SortOrder.Desc)
                    // Fields with null values are sorted last
                    .missing("_last")
                    // Fields with unmapped types doesn't throw an error and are mapped to Keyword
                    // Ex: When index is empty
                    .unmappedType(co.elastic.clients.elasticsearch._types.mapping.FieldType.Keyword)
                ))
            ))
            .toList())
        .orElse(List.of());

    return new SortBuilder(sortOptions);
  }

  /**
   * Creates a new {@link SortBuilder} with {@code language}
   */
  public List<SortOptions> build() {
    return sortOptions;

  }
}