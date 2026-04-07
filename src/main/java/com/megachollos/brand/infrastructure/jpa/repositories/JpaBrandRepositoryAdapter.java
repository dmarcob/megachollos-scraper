package com.megachollos.brand.infrastructure.jpa.repositories;

import com.megachollos.brand.domain.Brand;
import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaBrandRepositoryAdapter implements BrandRepository {

  private final JpaBrandRepository repository;

  @Override
  public Optional<Brand> findByUniqueName(String uniqueName) {
    return repository.findById(uniqueName).map(BrandEntity::toDomain);
  }

  @Override
  public List<Brand> findAll() {
    return repository
        .findAll(Sort.by(Sort.Order.asc("uniqueName"))).stream()
        .map(BrandEntity::toDomain).toList();
  }
}