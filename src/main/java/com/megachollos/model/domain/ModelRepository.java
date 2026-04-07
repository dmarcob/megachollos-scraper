package com.megachollos.model.domain;

import com.megachollos.category.domain.Category;
import java.util.List;
import java.util.Optional;

public interface ModelRepository {

  List<Model> findAll();

  Optional<Model> findById(Long id);

  List<Model> findByCategoryIn(List<Category> categories);

}
