package com.megachollos.category.jpa.repositories;

import com.megachollos.category.jpa.entities.CategoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, String> {

  List<CategoryEntity> findByParentCategoryUniqueName(String parentCategoryUniqueName);
}
