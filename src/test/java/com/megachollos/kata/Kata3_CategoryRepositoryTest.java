package com.megachollos.kata;

import com.megachollos.category.jpa.entities.CategoryEntity;
import com.megachollos.category.jpa.repositories.JpaCategoryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public class Kata3_CategoryRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindCategory(){

        CategoryEntity category = CategoryEntity.builder()
                .uniqueName("electronica")
                .displayName("Electronica")
                .build();

        jpaCategoryRepository.saveAndFlush(category);

        var result = jpaCategoryRepository.findById("electronica");

        assertThat(result).isPresent();
        assertThat(result.get().getUniqueName()).isEqualTo("electronica");
        assertThat(result.get().getDisplayName()).isEqualTo("Electronica");
    }

    @Test
    void shouldSaveCategoryWithParent(){

        CategoryEntity categoryParent = CategoryEntity.builder()
                .uniqueName("tech")
                .displayName("Technology")
                .build();

        jpaCategoryRepository.saveAndFlush(categoryParent);

        CategoryEntity categoryChild = CategoryEntity.builder()
                .uniqueName("laptops")
                .displayName("Laptops")
                .parentCategoryUniqueName("tech")
                .build();

        jpaCategoryRepository.saveAndFlush(categoryChild);

        entityManager.clear();

        var result = jpaCategoryRepository.findById("laptops");
        assertThat(result).isPresent();

        CategoryEntity category = result.get();

        assertThat(category.getParentCategory()).isNotNull();
        assertThat(category.getParentCategory().getUniqueName()).isEqualTo("tech");
    }

    @Test
    @Sql("/seed-categories.sql")
    void shouldFindSeededCategories() {

        long count = jpaCategoryRepository.count();
        assertThat(count).isEqualTo(3);

        var result = jpaCategoryRepository.findById("child1");

        assertThat(result).isPresent();

        CategoryEntity category = result.get();
        assertThat(category.getParentCategory()).isNotNull();
        assertThat(category.getParentCategory().getUniqueName()).isEqualTo("root1");
    }
}
