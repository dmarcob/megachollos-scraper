package com.megachollos.model.jpa.repositories;

import com.megachollos.category.domain.Category;
import com.megachollos.model.domain.Model;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.model.jpa.entities.ModelEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaModelRepositoryAdapter implements ModelRepository {

  private final JpaModelRepository repository;

  @Override
  public List<Model> findAll() {
    return repository.findAll().stream().map(ModelEntity::toDomain).toList();
  }

  @Override
  public Optional<Model> findById(Long id) {
    return repository.findById(id).map(ModelEntity::toDomain);
  }

  @Override
  public List<Model> findByCategoryIn(List<Category> categories) {
    Set<String> categoryUniqueNames = categories.stream()
        .map(Category::getUniqueName).collect(Collectors.toSet());

    return repository.findByCategoryIdIn(categoryUniqueNames).stream()
        .map(ModelEntity::toDomain).toList();
  }
}