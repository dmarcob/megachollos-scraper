package com.megachollos.category.jpa.repositories;

import com.megachollos.category.domain.Category;
import com.megachollos.category.domain.CategoryRepository;
import com.megachollos.category.jpa.entities.CategoryEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaCategoryRepositoryAdapter implements CategoryRepository {

  private final JpaCategoryRepository repository;

  @Override
  public List<Category> findByParentCategory(Category parentCategory) {
    return repository.findByParentCategoryUniqueName(parentCategory.getUniqueName()).stream()
        .map(CategoryEntity::toDomain).toList();
  }

  @Override
  public Optional<Category> findByUniqueName(String uniqueName) {
    return repository.findById(uniqueName).map(CategoryEntity::toDomain);
  }
}