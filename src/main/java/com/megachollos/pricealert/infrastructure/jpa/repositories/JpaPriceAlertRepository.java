package com.megachollos.pricealert.infrastructure.jpa.repositories;

import com.megachollos.pricealert.infrastructure.jpa.entities.PriceAlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPriceAlertRepository extends JpaRepository<PriceAlertEntity, Long> {
}
