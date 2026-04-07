package com.megachollos.shared.infrastructure.rest.dtos.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.util.Assert;

@Data
@With
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SearchResult<T> {

  private Scroll scroll;
  private List<Filter> filters;
  private List<Sort> sorts;
  private List<T> elements;
  private Long totalElements;

  public <U> SearchResult<U> map(Function<? super T, ? extends U> converter) {

    Assert.notNull(converter, "Function must not be null");

    List<U> mappedElements = Optional.ofNullable(elements)
        .orElse(new ArrayList<>())
        .stream()
        .map(converter)
        .collect(Collectors.toList());

    return SearchResult.<U>builder()
        .scroll(scroll)
        .filters(filters)
        .sorts(sorts)
        .totalElements(totalElements)
        .elements(mappedElements)
        .build();
  }
}