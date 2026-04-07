package com.megachollos.brand.infrastructure.jpa.repositories;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandRepository extends JpaRepository<BrandEntity, String> {

}
