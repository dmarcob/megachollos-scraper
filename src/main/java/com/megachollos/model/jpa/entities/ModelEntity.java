package com.megachollos.model.jpa.entities;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.category.jpa.entities.CategoryEntity;
import com.megachollos.model.domain.Model;
import com.megachollos.shared.infrastructure.entities.LinceSequenceValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;


@Entity(name = "MODELS")
@Builder
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelEntity {

  @Id
  @LinceSequenceValue
  private Long id;

  @Column(nullable = false)
  private String displayName;

  @Column(name = "CATEGORY")
  private String categoryId;

  @Column(name = "BRAND")
  private String brandId;

  @ManyToOne
  @JoinColumn(name = "CATEGORY", insertable = false, updatable = false, nullable = false)
  private CategoryEntity category;

  @ManyToOne
  @JoinColumn(name = "BRAND", insertable = false, updatable = false, nullable = false)
  private BrandEntity brand;

  public Model toDomain() {
    return Model.builder()
        .id(id)
        .displayName(displayName)
        .category(category.toDomain())
        .brand(brand.toDomain())
        .build();
  }
}
