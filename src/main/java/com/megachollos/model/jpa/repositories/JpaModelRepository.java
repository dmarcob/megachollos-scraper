package com.megachollos.model.jpa.repositories;

import com.megachollos.model.jpa.entities.ModelEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaModelRepository extends JpaRepository<ModelEntity, Long> {

  List<ModelEntity> findByCategoryIdIn(Set<String> categories);
}
