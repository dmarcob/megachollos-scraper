package com.megachollos.brand.infrastructure.jpa.entities;

import com.megachollos.brand.domain.Brand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;


@Entity(name = "BRANDS")
@Builder
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {

  @Id
  private String uniqueName;

  @Column(nullable = false)
  private String displayName;

  public Brand toDomain() {
    return Brand.builder()
        .uniqueName(uniqueName)
        .displayName(displayName)
        .build();
  }
}
