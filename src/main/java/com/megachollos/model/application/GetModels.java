package com.megachollos.model.application;

import com.megachollos.category.application.GetCategory;
import com.megachollos.category.application.GetLeafCategories;
import com.megachollos.category.domain.Category;
import com.megachollos.model.domain.Model;
import com.megachollos.model.domain.ModelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetModels {

  private final ModelRepository modelRepository;
  private final GetCategory getCategory;
  private final GetLeafCategories getLeafCategories;

  public List<Model> getAll() {
    return modelRepository.findAll();
  }

  public List<Model> getFromCategories(List<String> categories) {
    List<Category> leafCategoriies = categories.stream()
        .map(getCategory::getCategory)
        .flatMap(category -> getLeafCategories.getLeafCategories(category).stream())
        .toList();

    return modelRepository.findByCategoryIn(leafCategoriies);
  }
}
