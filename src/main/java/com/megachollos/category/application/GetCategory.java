package com.megachollos.category.application;

import static com.megachollos.category.domain.errors.CategoryError.CATEGORY_NOT_EXIST;

import com.megachollos.category.domain.Category;
import com.megachollos.category.domain.CategoryRepository;
import com.megachollos.shared.domain.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCategory {

  private final CategoryRepository categoryRepository;

  public Category getCategory(String uniqueName) {
    return categoryRepository.findByUniqueName(uniqueName)
        .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_EXIST, uniqueName));
  }
}
