package com.megachollos.category.application;

import static com.megachollos.testUtils.providers.CategoryProvider.getCategory;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.megachollos.category.domain.CategoryRepository;
import com.megachollos.shared.domain.exceptions.NotFoundException;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GetCategoryTest extends BaseUnitTest {

  @InjectMocks
  private GetCategory getCategory;

  @Mock
  private CategoryRepository categoryRepository;

  @Test
  void getCategory_OK() {
    // GIVEN
    when(categoryRepository.findByUniqueName(any())).thenReturn(Optional.of(getCategory()));

    // WHEN, THEN
    assertDoesNotThrow(() -> getCategory.getCategory(getCategory().getUniqueName()));
  }

  @Test
  void getCategory_doesNotExist_throwsException() {
    // GIVEN
    when(categoryRepository.findByUniqueName(any())).thenReturn(Optional.empty());

    // WHEN, THEN
    assertThrows(NotFoundException.class,
        () -> getCategory.getCategory(getCategory().getUniqueName()));
  }
}
