package com.megachollos.category.application;

import com.megachollos.category.domain.Category;
import com.megachollos.category.domain.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLeafCategories {

  private final CategoryRepository categoryRepository;

  /**
   * Retrieves the leaf categories (final descendants) of a given category in the tree. A leaf node
   * is defined as a node that has no children.
   */
  public List<Category> getLeafCategories(Category category) {
    List<Category> leafCategories = new ArrayList<>();

    List<Category> children = categoryRepository.findByParentCategory(category);

    if (children.isEmpty()) {
      leafCategories.add(category);
      return leafCategories;
    }

    children.forEach(child -> leafCategories.addAll(getLeafCategories(child)));
    return leafCategories;
  }
}
