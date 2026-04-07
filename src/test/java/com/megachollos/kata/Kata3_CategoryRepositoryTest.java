package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.category.jpa.entities.CategoryEntity;
import com.megachollos.category.jpa.repositories.JpaCategoryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class Kata3_CategoryRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

  @Autowired
  private JpaCategoryRepository categoryRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @Test
  void shouldSaveAndFindCategory() {
    CategoryEntity categoryToPersist = CategoryEntity.builder()
        .uniqueName("electronica")
        .displayName("Electronica")
        .build();

    categoryRepository.save(categoryToPersist);

    var persistedCategory = categoryRepository.findById("electronica");
    assertThat(persistedCategory).isPresent();
    assertThat(persistedCategory.get().getUniqueName()).isEqualTo("electronica");
    assertThat(persistedCategory.get().getDisplayName()).isEqualTo("Electronica");
  }

  @Test
  void shouldSaveCategoryWithParent() {
    CategoryEntity parentCategory = CategoryEntity.builder()
        .uniqueName("tech")
        .displayName("Technology")
        .build();
    categoryRepository.save(parentCategory);

    CategoryEntity childCategory = CategoryEntity.builder()
        .uniqueName("laptops")
        .displayName("Laptops")
        .parentCategoryUniqueName("tech")
        .build();
    categoryRepository.saveAndFlush(childCategory);

    testEntityManager.clear();

    var persistedChildCategory = categoryRepository.findById("laptops");
    assertThat(persistedChildCategory).isPresent();
    assertThat(persistedChildCategory.get().getParentCategory()).isNotNull();
    assertThat(persistedChildCategory.get().getParentCategory().getUniqueName()).isEqualTo("tech");
  }

  @Test
  @Sql("/seed-categories.sql")
  void shouldFindSeededCategories() {
    assertThat(categoryRepository.count()).isEqualTo(3);

    var seededChildCategory = categoryRepository.findById("child1");
    assertThat(seededChildCategory).isPresent();
    assertThat(seededChildCategory.get().getParentCategoryUniqueName()).isEqualTo("root1");
  }

  @Test
  @Sql("/seed-categories.sql")
  void shouldFindRootCategories() {
    List<CategoryEntity> rootCategories = categoryRepository.findAll().stream()
        .filter(category -> category.getParentCategoryUniqueName() == null)
        .toList();

    assertThat(rootCategories).hasSize(2);
  }
}
