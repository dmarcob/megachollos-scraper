package com.megachollos.category.application;

import static com.megachollos.testUtils.providers.CategoryProvider.getCategory;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.megachollos.category.domain.Category;
import com.megachollos.category.domain.CategoryRepository;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetLeafCategoriesTest extends BaseUnitTest {

  @InjectMocks
  private GetLeafCategories getLeafCategories;

  @Mock
  private CategoryRepository categoryRepository;

  @Test
  void getLeafCategories_isLeaf_OK() {
    // GIVEN
    when(categoryRepository.findByParentCategory(any())).thenReturn(List.of());

    // WHEN
    List<Category> result = getLeafCategories.getLeafCategories(getCategory());

    // THEN
    assertThat(result).hasSize(1);
  }

  @Test
  void getLeafCategories_isNotLeaf_OK() {
    // GIVEN
    when(categoryRepository.findByParentCategory(any()))
        .thenReturn(List.of(getCategory(), getCategory()))
        .thenReturn(List.of());

    // WHEN
    List<Category> result = getLeafCategories.getLeafCategories(getCategory());

    // THEN
    assertThat(result).hasSize(2);
  }
}
