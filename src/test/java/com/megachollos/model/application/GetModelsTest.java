package com.megachollos.model.application;

import static com.megachollos.testUtils.providers.CategoryProvider.getCategory;
import static com.megachollos.testUtils.providers.ModelProvider.getModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.megachollos.category.application.GetCategory;
import com.megachollos.category.application.GetLeafCategories;
import com.megachollos.model.domain.Model;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetModelsTest extends BaseUnitTest {

  @InjectMocks
  private GetModels getModels;

  @Mock
  private ModelRepository modelRepository;

  @Mock
  private GetCategory getCategory;

  @Mock
  private GetLeafCategories getLeafCategories;

  @Test
  void getAll_OK() {
    // GIVEN
    when(modelRepository.findAll()).thenReturn(List.of(getModel()));

    // WHEN
    List<Model> result = getModels.getAll();

    // THEN
    assertThat(result).isNotNull().hasSize(1);
  }

  @Test
  void getFromCategories_OK() {
    // GIVEN
    when(getCategory.getCategory(any())).thenReturn(getCategory());
    when(getLeafCategories.getLeafCategories(any())).thenReturn(List.of(getCategory()));
    when(modelRepository.findByCategoryIn(any())).thenReturn(List.of(getModel()));

    // WHEN
    List<Model> result = getModels.getFromCategories(List.of("tecnologia"));

    // THEN
    assertThat(result).isNotNull().hasSize(1);
  }
}
