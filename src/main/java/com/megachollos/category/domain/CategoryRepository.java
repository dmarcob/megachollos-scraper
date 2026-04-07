package com.megachollos.category.domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

  List<Category> findByParentCategory(Category parentCategory);

  Optional<Category> findByUniqueName(String uniqueName);
}
