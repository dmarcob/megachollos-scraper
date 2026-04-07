package com.megachollos.category.jpa.entities;

import com.megachollos.category.domain.Category;
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


@Entity(name = "CATEGORIES")
@Builder
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {

  @Id
  private String uniqueName;

  @Column(nullable = false)
  private String displayName;

  @Column(name = "PARENT_CATEGORY")
  private String parentCategoryUniqueName;

  @ManyToOne
  @JoinColumn(name = "PARENT_CATEGORY", insertable = false, updatable = false)
  private CategoryEntity parentCategory;

  public Category toDomain() {
    return Category.builder()
        .uniqueName(uniqueName)
        .displayName(displayName)
        .parentCategory(parentCategory == null ? null : parentCategory.toDomain())
        .build();
  }

  public static CategoryEntity fromDomain(Category category) {
    return CategoryEntity.builder()
        .uniqueName(category.getUniqueName())
        .displayName(category.getDisplayName())
        .parentCategory(
            category.getParentCategory() == null ? null : fromDomain(category.getParentCategory()))
        .build();
  }
}
