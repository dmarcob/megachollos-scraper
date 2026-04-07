package com.megachollos.testUtils.providers;

import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.shared.infrastructure.rest.dtos.search.FilterOperator;
import com.megachollos.shared.infrastructure.rest.dtos.search.Scroll;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.search.Sort;
import com.megachollos.shared.infrastructure.rest.dtos.search.SortOrder;
import java.util.List;

public class SearchProvider {

  private final static Integer SCROLL_SIZE = 10;
  private final static Long SCROLL_TIME = 60000L;
  private final static String FIELD = "reference";
  private final static String VALUE = "value";

  public static SearchCriteria getSearchCriteria() {
    return SearchCriteria.builder()
        .scroll(getScroll())
        .filters(List.of(getFilter()))
        .sorts(List.of(getSort()))
        .build();
  }

  public static <T> SearchResult<T> getSearchResult(List<T> elements) {
    return SearchResult.<T>builder()
        .elements(elements)
        .scroll(getScroll())
        .build();
  }

  public static Scroll getScroll() {
    return Scroll.builder()
        .scrollId(null)
        .scrollSize(SCROLL_SIZE)
        .scrollTimeInMillis(SCROLL_TIME)
        .build();
  }

  public static Sort getSort() {
    return Sort.builder()
        .sort(SortOrder.ASC)
        .field(FIELD)
        .build();
  }

  public static Filter getFilter() {
    return Filter.builder()
        .field(FIELD)
        .operator(FilterOperator.EQUAL)
        .value(VALUE)
        .build();
  }
}
